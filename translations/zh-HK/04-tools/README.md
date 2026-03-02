# Module 04: AI Agents with Tools

## 目錄

- [你將學習什麼](../../../04-tools)
- [先備條件](../../../04-tools)
- [了解帶工具的 AI Agents](../../../04-tools)
- [工具調用如何運作](../../../04-tools)
  - [工具定義](../../../04-tools)
  - [決策制定](../../../04-tools)
  - [執行](../../../04-tools)
  - [回應生成](../../../04-tools)
  - [架構：Spring Boot 自動連接](../../../04-tools)
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
- [何時使用基於工具的 Agents](../../../04-tools)
- [工具 vs RAG](../../../04-tools)
- [後續步驟](../../../04-tools)

## 你將學習什麼

到目前為止，你已經學會如何與 AI 對話、有效結構提示，以及將回應根據你的文件來定位。但仍存在基本限制：語言模型只能生成文字。它無法查詢天氣、執行計算、查詢資料庫或與外部系統互動。

工具改變了這點。給模型可調用的函數，將它從文字生成器轉變成可採取行動的代理。模型決定何時需要工具、使用哪個工具以及傳遞什麼參數。你的程式碼執行該函數並返回結果。模型將結果融入回應中。

## 先備條件

- 完成 [Module 01 - 介紹](../01-introduction/README.md)（Azure OpenAI 資源已部署）
- 建議完成先前模組（本模組在工具 vs RAG 比較中參考了 [Module 03 的 RAG 概念](../03-rag/README.md)）
- 根目錄下有 `.env` 檔案，包含 Azure 認證（由 Module 01 中的 `azd up` 建立）

> **注意：** 若尚未完成 Module 01，請先依照那裡的部署說明操作。

## 了解帶工具的 AI Agents

> **📝 注意：** 本模組中的「agents」指的是具備工具調用能力的 AI 助手，這與我們將在 [Module 05: MCP](../05-mcp/README.md) 涵蓋的 **Agentic AI** 模式（具有規劃、記憶、多步推理的自主代理）不同。

沒有工具時，語言模型只能根據訓練資料生成文字。問它當前天氣，它只能猜測。給它工具，它就能調用天氣 API、執行計算或查詢資料庫，然後將實際結果編織進回應中。

<img src="../../../translated_images/zh-HK/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*沒有工具時模型只能猜測 — 有了工具它可以調用 API、執行計算並返回即時資料。*

具工具的 AI 代理遵循一種 **推理與行動（ReAct）** 模式。模型不僅回應，它還會思考需要什麼、通過調用工具行動、觀察結果，然後決定是否再次行動或給出最終答案：

1. **推理** — 代理分析用戶問題，判斷所需資訊
2. **行動** — 選擇合適工具，生成正確參數並調用
3. **觀察** — 接收工具輸出並評估結果
4. **重複或回應** — 若需更多資料則循環，否則生成自然語言答案

<img src="../../../translated_images/zh-HK/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct 迴圈 — 代理推理如何行動，調用工具，觀察結果，迴圈直到產生最終答案。*

這個過程是自動化的。你定義工具和描述，模型決定何時以及如何使用它們。

## 工具調用如何運作

### 工具定義

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

你定義具明確描述和參數規範的函數。模型在系統提示中見到這些描述，理解每個工具的作用。

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

// 助手由 Spring Boot 自動連接包括：
// - ChatModel bean
// - 所有來自 @Component 類別的 @Tool 方法
// - 用於會話管理的 ChatMemoryProvider
```

下面的圖表解析每個註解，顯示如何協助 AI 理解何時調用工具及傳遞哪些參數：

<img src="../../../translated_images/zh-HK/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*工具定義結構 — @Tool 告訴 AI 何時使用，@P 描述每個參數，@AiService 在啟動時負責連接所有部分。*

> **🤖 可用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 開啟 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java)，試問：
> - 「如何整合像 OpenWeatherMap 這樣的真實天氣 API，取代模擬資料？」
> - 「什麼樣的工具描述能幫助 AI 正確使用它？」
> - 「如何在工具實現中處理 API 錯誤和速率限制？」

### 決策制定

當用戶問「西雅圖天氣如何？」時，模型不會亂選工具，而是比較每個工具描述與用戶意圖的相關性，給予分數，選擇最匹配的。它然後生成結構化函數調用和正確參數——此例中將 `location` 設為 `"Seattle"`。

若無工具匹配，模型回退用自己的知識回答；若多工具匹配，則選擇最具體者。

<img src="../../../translated_images/zh-HK/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*模型評估所有可用工具與用戶意圖的匹配度，選擇最佳匹配——這也說明為什麼寫清晰具體的工具描述很重要。*

### 執行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 會自動將所有註冊過的工具，以聲明式的 `@AiService` 介面注入，LangChain4j 負責自動執行工具調用。背後完整的調用流程啟動於用戶語言問題，回傳自然語言答案，經過六個步驟：

<img src="../../../translated_images/zh-HK/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*完整的端到端流程 — 用戶提問，模型選工具，LangChain4j 執行，模型將結果整合成自然語言回應。*

> **🤖 可用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 開啟 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)，試問：
> - 「ReAct 模式如何運作？為何它對 AI 代理有效？」
> - 「代理如何決定使用哪些工具及執行順序？」
> - 「若工具執行失敗，該如何健壯處理錯誤？」

### 回應生成

模型接收天氣數據，並格式化為用戶可讀的自然語言回應。

### 架構：Spring Boot 自動連接

本模組使用 LangChain4j 的 Spring Boot 整合及宣告式 `@AiService` 介面。啟動時 Spring Boot 掃描含有 `@Tool` 方法的所有 `@Component`、你的 `ChatModel` bean 與 `ChatMemoryProvider`，並將它們注入至單一 `Assistant` 介面，完全免除樣板碼。

<img src="../../../translated_images/zh-HK/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService 介面將 ChatModel、工具組件及記憶體提供者串接起來 — Spring Boot 自動處理所有連接。*

此作法的主要優勢：

- **Spring Boot 自動注入** — ChatModel 和工具自動植入
- **@MemoryId 模式** — 自動的基於會話的記憶管理
- **單一實例** — Assistant 實例唯一且重用，提高效能
- **型別安全執行** — 直接調用 Java 方法並自動型別轉換
- **多輪協調** — 自動處理工具串聯
- **零樣板碼** — 不需要手動呼叫 `AiServices.builder()` 或維護記憶 HashMap

另一種手動 `AiServices.builder()` 的方法會寫更多程式碼且無法享有 Spring Boot 整合的優勢。

## 工具串聯

**工具串聯** — 基於工具的代理最大威力體現於單一問題需多個工具時。問「西雅圖的天氣用華氏度是多少？」時，代理會自動串聯兩個工具：先調用 `getCurrentWeather` 取得攝氏溫度，然後將溫度傳給 `celsiusToFahrenheit` 做轉換 — 全在同一對話輪次完成。

<img src="../../../translated_images/zh-HK/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*工具串聯實例 — 代理先調用 getCurrentWeather，然後將攝氏結果傳給 celsiusToFahrenheit，最後給出合成答案。*

**平滑失敗** — 若查詢非模擬資料內的城市，工具會回傳錯誤訊息，AI 會解釋無法協助，而非崩潰。工具安全失敗。下圖對比了兩種情況：妥善處理錯誤代理捕捉例外並給出友善回應，否則整個應用崩潰：

<img src="../../../translated_images/zh-HK/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*工具失敗時，代理捕獲錯誤並以友善說明回應，避免程式崩潰。*

這都在單一對話輪次完成。代理自主協調多個工具調用。

## 運行應用程式

**確認部署狀態：**

確保根目錄有 `.env` 檔案且包含 Azure 認證（在 Module 01 中建立）。從本模組目錄（`04-tools/`）執行：

**Bash:**
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若你已從根目錄使用 `./start-all.sh` 啟動所有應用（如 Module 01 所述），本模組已於 8084 端口運行，則可跳過以下啟動指令，直接瀏覽 http://localhost:8084。

**方案一：使用 Spring Boot Dashboard（建議 VS Code 使用者）**

開發容器包含 Spring Boot Dashboard 擴展，提供視覺化介面管理所有 Spring Boot 應用。你可在 VS Code 左側活動欄找到（尋找 Spring Boot 圖示）。

在 Spring Boot Dashboard 你可以：
- 查看工作區中所有 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時觀看應用日誌
- 監控應用狀態

點選「tools」旁的播放按鈕啟動本模組，或一次啟動所有模組。

這是 VS Code 中的 Spring Boot Dashboard 外觀：

<img src="../../../translated_images/zh-HK/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot Dashboard — 從同一處啟動、停止並監控所有模組*

**方案二：使用 shell 腳本**

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

兩個腳本均自動從根目錄 `.env` 載入環境變數，如 JAR 檔不存在會自動建置。

> **注意：** 若你偏好先手動建置所有模組後才啟動：
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

在瀏覽器打開 http://localhost:8084 。

**停止：**

**Bash:**
```bash
./stop.sh  # 僅此模組
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

此應用提供網頁介面，讓你與具備天氣及溫度轉換工具的 AI 代理互動。下圖為介面示範—含快速啟動範例和用於發送請求的聊天面板：
<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-HK/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools 介面" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools 介面 - 快速示範及與工具互動的對話介面*

### 嘗試簡單的工具使用

從一個簡單的請求開始：「將100華氏度轉換為攝氏度」。代理識別到需要使用溫度轉換工具，以正確的參數調用，並傳回結果。注意這過程有多自然——你不需指定使用哪個工具或如何調用它。

### 測試工具串接

現在試試更複雜的請求：「西雅圖的天氣如何並轉換成華氏度？」觀察代理如何分步處理。它先獲取天氣（回傳攝氏度），識別到需要轉換成華氏度，呼叫轉換工具，並將兩個結果合併成一個答案。

### 查看對話流程

聊天介面會保留對話歷史，讓你可以進行多輪互動。你可以看到之前所有查詢與回應，方便追蹤對話並了解代理如何在多次交流中建立上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-HK/tools-conversation-demo.89f2ce9676080f59.webp" alt="多次呼叫工具的對話" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多輪對話展示簡單轉換、天氣查詢與工具串接*

### 嘗試不同的請求

試試不同組合：

- 天氣查詢：「東京的天氣如何？」
- 溫度轉換：「25°C是多少開爾文？」
- 結合查詢：「查詢巴黎天氣，告訴我是否高於20°C」

留意代理如何將自然語言解讀並映射到適當的工具呼叫。

## 主要概念

### ReAct 模式（推理與行動）

代理在推理（決定該做什麼）與行動（使用工具）間交替。此模式使代理能自主解決問題，而非只是響應指令。

### 工具描述很重要

工具描述的品質會直接影響代理使用工具的效果。清晰、具體的描述有助模型理解何時及如何調用每個工具。

### 會話管理

`@MemoryId` 註解啟用自動會話記憶管理。每個會話 ID 都有自己的 `ChatMemory` 實例，由 `ChatMemoryProvider` bean 管理，讓多位用戶可同時與代理互動，彼此對話內容互不混淆。下圖展示多用戶如何根據會話 ID 被導向獨立記憶存儲：

<img src="../../../translated_images/zh-HK/session-management.91ad819c6c89c400.webp" alt="使用 @MemoryId 的會話管理" width="800"/>

*每個會話 ID 對應獨立的對話歷史 — 用戶間不會看到對方的訊息。*

### 錯誤處理

工具可能失敗——API 超時、參數錯誤、外部服務故障。生產環境代理需有錯誤處理機制，讓模型能解釋問題或嘗試替代方案，而不會使整個應用崩潰。當工具拋出例外時，LangChain4j 會捕捉並將錯誤訊息回傳給模型，模型隨後可用自然語言說明問題。

## 可用的工具

下圖顯示你可以建立的廣泛工具生態系。此模組示範天氣與溫度工具，但相同的 `@Tool` 模式適用於任何 Java 方法——從資料庫查詢到支付處理。

<img src="../../../translated_images/zh-HK/tool-ecosystem.aad3d74eaa14a44f.webp" alt="工具生態系" width="800"/>

*凡使用 @Tool 註解的 Java 方法都能提供給 AI 使用——此模式可延伸到資料庫、APIs、電郵、檔案操作等。*

## 何時使用基於工具的代理

非所有請求都需要工具。判斷依據是 AI 是否需要與外部系統互動，或能否自行回答。以下指南總結何時工具有價值，何時不需用到：

<img src="../../../translated_images/zh-HK/when-to-use-tools.51d1592d9cbdae9c.webp" alt="何時使用工具" width="800"/>

*快速決策指南——工具適用於實時資料、計算與操作；一般知識與創意任務則不需。*

## 工具 vs RAG

模組 03 與 04 都擴展了 AI 的能力，但方式根本不同。RAG 讓模型透過檢索文件取得**知識**。工具賦予模型藉由調用函數執行**動作**的能力。下圖並列比較這兩種方法——從工作流程到各自的優缺點：

<img src="../../../translated_images/zh-HK/tools-vs-rag.ad55ce10d7e4da87.webp" alt="工具 vs RAG 比較" width="800"/>

*RAG 從靜態文件中檢索資訊——工具執行動作並取得動態、即時資料。許多生產系統會將兩者結合使用。*

實務上，許多生產系統皆結合兩種方式：RAG 用於根據文件奠定答案根基，工具負責取得即時資料或執行操作。

## 下一步

**下一模組：** [05-mcp - 模型上下文協議 (MCP)](../05-mcp/README.md)

---

**導覽：** [← 上一章：模組 03 - RAG](../03-rag/README.md) | [回到主頁](../README.md) | [下一章：模組 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原文文件應視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->