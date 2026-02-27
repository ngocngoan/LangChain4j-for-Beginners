# Module 05: 模型上下文協議 (MCP)

## 目錄

- [你將學會什麼](../../../05-mcp)
- [什麼是 MCP？](../../../05-mcp)
- [MCP 如何運作](../../../05-mcp)
- [Agentic 模組](../../../05-mcp)
- [執行範例](../../../05-mcp)
  - [先決條件](../../../05-mcp)
- [快速開始](../../../05-mcp)
  - [檔案操作 (Stdio)](../../../05-mcp)
  - [主管代理](../../../05-mcp)
    - [執行示範](../../../05-mcp)
    - [主管如何運作](../../../05-mcp)
    - [回應策略](../../../05-mcp)
    - [了解輸出](../../../05-mcp)
    - [Agentic 模組功能說明](../../../05-mcp)
- [關鍵概念](../../../05-mcp)
- [恭喜！](../../../05-mcp)
  - [接下來呢？](../../../05-mcp)

## 你將學會什麼

你已經建立了對話式 AI，精通提示詞，讓回應基於文件，並使用工具創建代理。但所有這些工具都是為你的特定應用程序量身打造。如果你可以讓你的 AI 存取一個標準化的工具生態系統，而這些工具是任何人都能創建和分享的呢？在本模組中，你將學會如何利用模型上下文協議 (MCP) 以及 LangChain4j 的 agentic 模組來做到這一點。我們先展示一個簡單的 MCP 檔案讀取器，然後示範如何輕鬆地將它整合到先進的 agentic 工作流程中，使用主管代理模式。

## 什麼是 MCP？

模型上下文協議 (MCP) 就是提供這樣的標準方式，讓 AI 應用發現並使用外部工具。你不用為每個資料來源或服務寫自訂整合，而是連接到暴露其功能且格式一致的 MCP 伺服器。你的 AI 代理便可以自動發現並使用這些工具。

<img src="../../../translated_images/zh-HK/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 之前：複雜的點對點整合。MCP 之後：一個協議，無限可能。*

MCP 解決了 AI 開發中的一個根本問題：每個整合都是自訂的。想要存取 GitHub？自訂程式碼。想讀檔案？自訂程式碼。想查詢資料庫？自訂程式碼。且這些整合無法與其它 AI 應用相容。

MCP 將這標準化。一個 MCP 伺服器會以清楚的描述和架構暴露工具。任何 MCP 用戶端都能連接、發現可用工具並使用它們。一次建立，到處可用。

<img src="../../../translated_images/zh-HK/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型上下文協議架構 — 標準化工具的發現與執行*

## MCP 如何運作

<img src="../../../translated_images/zh-HK/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 底層運作 — 用戶端發現工具，交換 JSON-RPC 訊息，透過傳輸層執行操作。*

**伺服器-用戶端架構**

MCP 採用用戶端-伺服器模型。伺服器提供工具 — 讀檔案、查詢資料庫、呼叫 API。用戶端（你的 AI 應用）連接伺服器並使用它們的工具。

要在 LangChain4j 中使用 MCP，加入此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**工具發現**

當你的用戶端連接 MCP 伺服器時，它會問「你有哪些工具？」伺服器就回應可用工具清單，每個工具有說明和參數結構。你的 AI 代理即可根據用戶請求決定要使用哪個工具。

<img src="../../../translated_images/zh-HK/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在啟動時發現可用工具—它現在知道哪些功能可用，並可決定使用哪些。*

**傳輸機制**

MCP 支援不同的傳輸機制。本模組示範本地程序用的 Stdio 傳輸：

<img src="../../../translated_images/zh-HK/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 傳輸機制：遠端伺服器用 HTTP，本地程序用 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

適用於本地程序。你的應用啟動一個作為子程序的伺服器，並透過標準輸入/輸出溝通。適合檔案系統存取或命令列工具。

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

<img src="../../../translated_images/zh-HK/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 傳輸實例 — 你的應用啟動 MCP 伺服器作為子程序，並透過 stdin/stdout 管道溝通。*

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) 並問：
> - 「Stdio 傳輸是如何運作的？何時該用它而非 HTTP？」
> - 「LangChain4j 如何管理啟動的 MCP 伺服器程序生命週期？」
> - 「讓 AI 存取檔案系統有什麼安全性考量？」

## Agentic 模組

雖然 MCP 提供標準化工具，LangChain4j 的 **agentic 模組** 則提供宣告式方式來建立調度這些工具的代理。`@Agent` 註解和 `AgenticServices` 讓你用介面而非指令式程式碼定義代理行為。

在本模組中，你將探索 **主管代理 (Supervisor Agent)** 模式 — 一種進階 agentic AI 方法，由「主管」代理根據用戶請求動態決定呼叫哪些子代理。我們結合兩者概念，讓其中一個子代理擁有 MCP 驅動的檔案存取能力。

要使用 agentic 模組，加入此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ 實驗性功能：** `langchain4j-agentic` 模組屬於 **實驗階段**，可能會改變。穩定的 AI 助手建構仍推薦用 `langchain4j-core` 及自訂工具（模組 04）。

## 執行範例

### 先決條件

- Java 21+，Maven 3.9+
- Node.js 16+ 和 npm（用於 MCP 伺服器）
- 在 `.env` 檔中設定環境變數（根目錄）：
  - `AZURE_OPENAI_ENDPOINT`、`AZURE_OPENAI_API_KEY`、`AZURE_OPENAI_DEPLOYMENT`（與模組 01-04 相同）

> **注意：** 如果尚未設定環境變數，請參考 [模組 00 - 快速開始](../00-quick-start/README.md) 的說明，或將根目錄下 `.env.example` 複製為 `.env` 並填入你的值。

## 快速開始

**使用 VS Code:** 在檔案瀏覽器中，右鍵點選任一示範檔案，選擇 **「執行 Java」**，或使用執行與除錯面板的啟動配置（確保先在 `.env` 設定你的 token）。

**使用 Maven:** 你也可以從命令列執行以下範例。

### 檔案操作 (Stdio)

展示基於本地子程序的工具。

**✅ 無需先決條件**— MCP 伺服器自動產生。

**使用啟動腳本（推薦）：**

啟動腳本會自動從根 `.env` 加載環境變數：

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**使用 VS Code:** 右鍵 `StdioTransportDemo.java` 選擇 **「執行 Java」**（確保 `.env` 已配置）。

應用自動啟動檔案系統 MCP 伺服器並讀取本地檔案。注意子程序管理已替你處理。

**預期輸出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### 主管代理

**主管代理模式**是一種**靈活**的 agentic AI 形式。主管使用大語言模型 (LLM) 自主決定根據用戶請求該調用哪些代理。在下一個範例中，我們結合 MCP 驅動的檔案存取與 LLM 代理，打造一個受主管監督的檔案讀取→報告工作流程。

示範中，`FileAgent` 使用 MCP 檔案系統工具讀取檔案，`ReportAgent` 產生包含執行摘要（一句話）、三點重點及建議的結構化報告。主管自動調度此流程：

<img src="../../../translated_images/zh-HK/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*主管用其 LLM 決定要調用哪些代理及順序—無需硬編碼路由。*

我們的檔案轉報告工作流程實際樣貌如下：

<img src="../../../translated_images/zh-HK/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 利用 MCP 工具讀取檔案，然後 ReportAgent 將原始內容轉換成結構化報告。*

每個代理會將輸出存放在 **Agentic Scope**（共享記憶體），讓後續代理能存取先前結果。這展示 MCP 工具如何無縫整合到 agentic 工作流程 — 主管不必知道檔案怎麼讀，只要知道 `FileAgent` 能做到。

#### 執行示範

啟動腳本會自動從根 `.env` 加載環境變數：

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**使用 VS Code:** 右鍵 `SupervisorAgentDemo.java` 選擇 **「執行 Java」**（確保 `.env` 已配置）。

#### 主管如何運作

```java
// 第一步：FileAgent 使用 MCP 工具讀取檔案
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 有用於檔案操作的 MCP 工具
        .build();

// 第二步：ReportAgent 生成結構化報告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor 協調檔案 → 報告的工作流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 返回最終報告
        .build();

// Supervisor 根據請求決定調用哪些代理員
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### 回應策略

設定 `SupervisorAgent` 時，你會指定其在子代理完成任務後該如何形成對用戶的最終回答。

<img src="../../../translated_images/zh-HK/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*主管用三種策略形成最終回應 — 根據你想要最後代理輸出、彙整摘要或最高分選項來決定。*

可用策略如下：

| 策略 | 說明 |
|----------|-------------|
| **LAST** | 主管會回傳最後被調用子代理或工具的輸出。當工作流程中最後一個代理被專門設計為產生完整最終答案（例如研究流程中的「摘要代理」）時非常有用。 |
| **SUMMARY** | 主管使用其內建的語言模型 (LLM) 彙整整個互動及所有子代理輸出，並回傳此摘要作為最終回答，提供給用戶清晰的綜合答案。 |
| **SCORED** | 系統使用內部 LLM 分別對 LAST 的回答和 SUMMARY 進行評分，依原始用戶請求，回傳得分較高的輸出。 |

完整實現請參見 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 並問：
> - 「主管如何決定調用哪些代理？」
> - 「主管模式和順序工作流程模式有何不同？」
> - 「如何自訂主管的規劃行為？」

#### 了解輸出

執行示範時，你會看到主管如何多代理協調的結構化步驟說明。以下說明各段含意：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**標題**介紹工作流程概念：從檔案讀取到報告生成的專注管線。

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**工作流程圖**顯示代理間的資料流。每個代理有特定角色：
- **FileAgent** 使用 MCP 工具讀取檔案並將原始內容存於 `fileContent`
- **ReportAgent** 消費該內容並產生結構化報告於 `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**用戶請求**列出任務。主管解析此請求並決定調用 FileAgent → ReportAgent。

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**主管調度**展示兩步驟流程：
1. **FileAgent** 透過 MCP 讀取檔案並存內容
2. **ReportAgent** 收取內容並產生結構化報告

主管根據用戶請求**自主**作出這些決策。

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### Agentic 模組功能說明

此示例展示 agentic 模組幾項進階功能，讓我們仔細看看 Agentic Scope 和代理監聽器 (Agent Listeners)。

**Agentic Scope** 顯示共享記憶區，代理使用 `@Agent(outputKey="...")` 將結果存入此處。這允許：
- 後續代理能存取早前代理的輸出
- 主管能彙整最終回答
- 你能檢查各代理產出的內容

<img src="../../../translated_images/zh-HK/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope 充當共享記憶體 — FileAgent 寫入 `fileContent`，ReportAgent 讀取並寫入 `report`，而你的程式讀該最終結果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // 來自 FileAgent 的原始檔案數據
String report = scope.readState("report");            // 來自 ReportAgent 的結構化報告
```

**代理監聽器**能監控並除錯代理執行。示範中看到的逐步輸出來自掛載在每次代理呼叫的 AgentListener：
- **beforeAgentInvocation** - 當監督者選擇代理時調用，讓你看到選擇了哪個代理及原因
- **afterAgentInvocation** - 當代理完成時調用，顯示其結果
- **inheritedBySubagents** - 當為 true 時，監聽器會監控層級中的所有代理

<img src="../../../translated_images/zh-HK/agent-listeners.784bfc403c80ea13.webp" alt="代理監聽器生命週期" width="800"/>

*代理監聽器掛鉤於執行生命週期 — 監視代理啟動、完成或遇到錯誤的時機。*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // 傳播到所有子代理
    }
};
```

除了監督者模式，`langchain4j-agentic` 模組還提供多種強大的工作流程模式和功能：

<img src="../../../translated_images/zh-HK/workflow-patterns.82b2cc5b0c5edb22.webp" alt="代理工作流程模式" width="800"/>

*五種用於協調代理的工作流程模式 — 從簡單的順序流程到人機迴圈審核工作流程。*

| 模式 | 說明 | 使用案例 |
|---------|-------------|----------|
| **順序** | 按順序執行代理，輸出流向下一個 | 流水線：研究 → 分析 → 報告 |
| **並行** | 同時運行多個代理 | 獨立任務：天氣 + 新聞 + 股票 |
| **迴圈** | 迭代直到條件達成 | 品質評分：細化直到分數 ≥ 0.8 |
| **條件** | 基於條件路由 | 分類 → 路由至專家代理 |
| **人機迴圈** | 增加人工檢查點 | 審核工作流程、內容審查 |

## 關鍵概念

現在你已經探索過 MCP 和 agentic 模組的實際應用，讓我們總結何時使用各種方法。

<img src="../../../translated_images/zh-HK/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP 生態系統" width="800"/>

*MCP 打造通用協議生態系統 — 任何 MCP 相容的伺服器都能與任何 MCP 相容的客戶端合作，實現跨應用的工具共享。*

**MCP** 非常適合想利用現有工具生態系統、構建多個應用共享的工具、使用標準協議整合第三方服務，或在不更改程式碼的情況下替換工具實作時使用。

**Agentic 模組** 最佳應用場景是你需要用 `@Agent` 註解定義宣告式代理，需要工作流協調（順序、迴圈、並行）、偏好基於介面的代理設計而非命令式程式碼，或需要結合多個通過 `outputKey` 共享輸出的代理。

**監督者代理模式** 適合流程事前無法完全預測、希望由 LLM 決策、多個專門代理需要動態協調、打造路由至不同能 力的對話系統，或追求最靈活、自適應代理行為時使用。

<img src="../../../translated_images/zh-HK/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="自訂工具 vs MCP 工具" width="800"/>

*何時使用自訂 @Tool 方法與 MCP 工具 — 自訂工具用於特定應用邏輯並具完整型別安全，MCP 工具用於跨應用標準化整合。*

## 恭喜！

<img src="../../../translated_images/zh-HK/course-completion.48cd201f60ac7570.webp" alt="課程完成" width="800"/>

*你已完成五個模組的學習之旅 — 從基礎聊天到 MCP 驅動的 agentic 系統。*

你已完成 LangChain4j 初學者課程。你學會了：

- 如何用記憶建立對話 AI（模組 01）
- 不同任務的提示工程模式（模組 02）
- 用 RAG 讓回答根據文件內容基礎（模組 03）
- 使用自訂工具建立基本 AI 代理（助理）（模組 04）
- 用 LangChain4j MCP 和 Agentic 模組整合標準化工具（模組 05）

### 下一步？

完成模組後，探索 [Testing Guide](../docs/TESTING.md) 了解 LangChain4j 測試概念實作。

**官方資源：**
- [LangChain4j 文件](https://docs.langchain4j.dev/) - 全面指南與 API 參考
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - 原始碼與範例
- [LangChain4j 教學](https://docs.langchain4j.dev/tutorials/) - 各種用例的分步教學

感謝你完成本課程！

---

**導覽：** [← 上一節：模組 04 - 工具](../04-tools/README.md) | [回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保翻譯的準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的原文版本應被視為權威來源。對於重要資訊，建議使用專業人工翻譯。本公司對因使用本翻譯而引起的任何誤解或誤譯概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->