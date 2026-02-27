# Module 05: 模型上下文協議 (MCP)

## 目錄

- [你將學到什麼](../../../05-mcp)
- [什麼是 MCP？](../../../05-mcp)
- [MCP 如何運作](../../../05-mcp)
- [Agentic 模組](../../../05-mcp)
- [執行範例](../../../05-mcp)
  - [先決條件](../../../05-mcp)
- [快速開始](../../../05-mcp)
  - [檔案操作 (Stdio)](../../../05-mcp)
  - [監督代理](../../../05-mcp)
    - [執行示範](../../../05-mcp)
    - [監督如何運作](../../../05-mcp)
    - [回應策略](../../../05-mcp)
    - [理解輸出](../../../05-mcp)
    - [Agentic 模組功能說明](../../../05-mcp)
- [關鍵概念](../../../05-mcp)
- [恭喜！](../../../05-mcp)
  - [接下來？](../../../05-mcp)

## 你將學到什麼

你已經建構了對話式 AI，精通提示，並能根據文件提供有根據的回應，也建立了具備工具的代理。但這些工具都是為你的特定應用量身定做。如果你能讓 AI 能使用一套標準化生態系統中的工具，並且任何人都可以建立和分享呢？在這個模組中，你將學會如何利用模型上下文協議 (MCP) 和 LangChain4j 的 agentic 模組來做到這一點。我們首先展示簡單的 MCP 檔案閱讀器，然後展示它如何輕鬆地整合到使用監督代理模式的進階 agentic 工作流程中。

## 什麼是 MCP？

模型上下文協議 (MCP) 正是提供這個功能——為 AI 應用程式提供一種標準化的方法來發現和使用外部工具。你不用為每個資料來源或服務撰寫客製化整合，只要連接到以一致格式公開其功能的 MCP 伺服器，你的 AI 代理便能自動發現並使用這些工具。

<img src="../../../translated_images/zh-MO/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 之前：複雜的點對點整合。MCP 之後：一套協議，無限可能。*

MCP 解決了 AI 開發中的一個根本性問題：每個整合都是專屬的。想要存取 GitHub？得寫客製化程式碼。想要讀取檔案？客製化程式碼。想要查詢資料庫？還是客製化程式碼。而且這些整合都無法跨 AI 應用通用。

MCP 將這些標準化了。一個 MCP 伺服器會以清楚描述和結構化的 schema 來公開工具。任何 MCP 用戶端都能連接並發現可用工具，並加以使用。一次建立，處處適用。

<img src="../../../translated_images/zh-MO/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型上下文協議架構 - 標準化工具發現與執行*

## MCP 如何運作

<img src="../../../translated_images/zh-MO/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 背後的運作原理——用戶端發現工具，交換 JSON-RPC 訊息，並透過傳輸層執行操作。*

**伺服器-用戶端架構**

MCP 採用用戶端-伺服器模型。伺服器提供工具——讀取檔案、查詢資料庫、呼叫 API。用戶端（你的 AI 應用）連接伺服器並使用這些工具。

要在 LangChain4j 使用 MCP，加入此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```


**工具發現**

當用戶端連接到 MCP 伺服器時，會詢問：「你有哪些工具？」伺服器回應可用工具清單，每個工具都有描述和參數結構。你的 AI 代理可以根據使用者請求決定要用哪些工具。

<img src="../../../translated_images/zh-MO/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在啟動時發現可用工具——現在它知道有哪些功能可以使用，並能決定使用哪些工具。*

**傳輸機制**

MCP 支援不同的傳輸機制。本模組示範本地程序的 Stdio 傳輸：

<img src="../../../translated_images/zh-MO/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 傳輸機制：HTTP 用於遠端伺服器，Stdio 用於本地程序*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

用於本地程序。你的應用以子程序方式啟動伺服器，並透過標準輸入/輸出與之通訊。適用於檔案系統存取或命令行工具。

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


<img src="../../../translated_images/zh-MO/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 傳輸實際運作——應用作為子進程啟動 MCP 伺服器並透過 stdin/stdout 管道通訊。*

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat:** 打開 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) 並問：
> - 「Stdio 傳輸是如何運作的？什麼時候該用它，什麼時候用 HTTP？」
> - 「LangChain4j 如何管理啟動的 MCP 伺服器進程的生命週期？」
> - 「讓 AI 存取檔案系統有哪些安全性影響？」

## Agentic 模組

雖然 MCP 提供了標準化工具，LangChain4j 的 **agentic 模組** 則提供了用宣告式方式建立代理的能力，以協調這些工具。`@Agent` 註解與 `AgenticServices` 讓你可透過介面定義代理行為，而非命令式程式碼。

在本模組中，你將探索 **監督代理** 模式——一種進階 agentic AI 方法，其中「監督」代理基於用戶請求動態決定要呼叫哪些子代理。我們將融合這兩種概念，讓其中一個子代理具備 MCP 驅動的檔案存取功能。

要使用 agentic 模組，加入此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```


> **⚠️ 實驗性功能：** `langchain4j-agentic` 模組是 **實驗性**，可能會有變動。建構 AI 助手的穩定方法仍是使用 `langchain4j-core` 搭配自訂工具（模組 04）。

## 執行範例

### 先決條件

- Java 21+，Maven 3.9+
- Node.js 16+ 和 npm（用於 MCP 伺服器）
- 在 `.env` 檔案中配置環境變數（根目錄）：
  - `AZURE_OPENAI_ENDPOINT`、`AZURE_OPENAI_API_KEY`、`AZURE_OPENAI_DEPLOYMENT`（同模組 01-04）

> **注意：** 若尚未設定環境變數，請參照 [模組 00 - 快速開始](../00-quick-start/README.md) 的說明，或將 `.env.example` 複製為 `.env` 並填寫你的值。

## 快速開始

**使用 VS Code：** 在資源管理器中右鍵點任何示範檔案，選擇 **「執行 Java」**，或使用執行與偵錯面板中的啟動設定（先確保已在 `.env` 檔案配置好你的令牌）。

**使用 Maven：** 你也可以在命令行執行以下範例。

### 檔案操作 (Stdio)

這示範本地子程序的工具。

**✅ 不需先決條件** — MCP 伺服器會自動啟動。

**使用啟動腳本（推薦）：**

啟動腳本會自動從根目錄載入環境變數到 `.env` 檔案：

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

**使用 VS Code：** 右鍵點擊 `StdioTransportDemo.java`，選擇 **「執行 Java」**（確保 `.env` 設定完畢）。

應用程式會自動啟動檔案系統 MCP 伺服器並讀取本地檔案。注意子程序管理已為你處理。

**預期輸出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```


### 監督代理

**監督代理模式** 是一種 **靈活** 的 agentic AI。監督者使用 LLM 自主決定根據用戶請求要呼叫哪些代理。在下一個例子中，我們結合 MCP 支援的檔案存取和 LLM 代理，建立一個受監督的檔案讀取 → 報告工作流程。

示範中，`FileAgent` 使用 MCP 檔案系統工具讀取檔案，`ReportAgent` 提供包含執行摘要（一句話）、3 個重點和建議的結構化報告。監督者會自動協調此流程：

<img src="../../../translated_images/zh-MO/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*監督者用 LLM 判斷要呼叫哪些代理與順序——不需硬編碼工作流程。*

以下是我們從檔案到報告管線的具體流程：

<img src="../../../translated_images/zh-MO/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 透過 MCP 工具讀取檔案，然後 ReportAgent 將原始內容轉成結構化報告。*

每個代理在 **Agentic 範圍**（共享記憶體）中儲存輸出，允許下游代理讀取先前結果。這展示 MCP 工具如何無縫整合進 agentic 工作流程——監督者不需知道檔案如何讀取，只需知道 `FileAgent` 能做到。

#### 執行示範

啟動腳本會自動從根目錄載入環境變數：

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

**使用 VS Code：** 右鍵點擊 `SupervisorAgentDemo.java`，選擇 **「執行 Java」**（確保 `.env` 設定完畢）。

#### 監督如何運作

```java
// 第一步：FileAgent 使用 MCP 工具讀取檔案
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 擁有用於檔案操作的 MCP 工具
        .build();

// 第二步：ReportAgent 產生結構化報告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// 主管負責協調檔案→報告的工作流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 傳回最終報告
        .build();

// 主管根據請求決定要召喚哪些代理程式
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```


#### 回應策略

當你配置 `SupervisorAgent` 時，要指定子代理完成任務後如何形成給使用者的最終回應。

<img src="../../../translated_images/zh-MO/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*三種監督者形成最終回應的策略——依你希望得到最後代理輸出、綜合摘要，或是最高分選項來決定。*

可用策略包括：

| 策略 | 描述 |
|----------|-------------|
| **LAST** | 監督者返回最後一個子代理或工具的輸出。當工作流程中最後一個代理專門產生完整最終答案時最有用（如研究管線中的「摘要代理」）。 |
| **SUMMARY** | 監督者使用自身內部的語言模型 (LLM) 綜合所有互動和子代理輸出，形成摘要並作為最終回應，提供乾淨且聚合的答案。 |
| **SCORED** | 系統利用內部 LLM 分別對 LAST 回應和 SUMMARY 進行評分，根據原始用戶請求選擇得分較高的結果返回。 |

完整實作請參見 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat:** 打開 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 並問：
> - 「監督者如何決定要呼叫哪些代理？」
> - 「監督者模式和序列工作流程模式有何不同？」
> - 「如何自訂監督者的規劃行為？」

#### 理解輸出

執行示範時，你將看到監督者如何協調多個代理的結構化解說。各部分說明如下：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```


**標題**介紹了工作流程概念：一條專注的管線，從檔案讀取到報告產生。

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


**工作流程圖**展示代理間的資料流。每個代理有明確角色：
- **FileAgent** 使用 MCP 工具讀取檔案並將原始內容存到 `fileContent`
- **ReportAgent** 消耗該內容並產生結構化報告存在 `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```


**用戶請求**展示任務。監督者解析此請求並決定依序呼叫 FileAgent → ReportAgent。

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


**監督協調**呈現兩步驟流程：
1. **FileAgent** 透過 MCP 讀取檔案並保存內容
2. **ReportAgent** 接收內容並產生結構化報告

監督者完全根據用戶請求 **自主** 做出決策。

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

範例展示 agentic 模組的多項進階功能。我們先來看看 Agentic 範圍和代理監聽器。

**Agentic 範圍**顯示了代理使用 `@Agent(outputKey="...")` 儲存結果的共享記憶體。這使得：
- 後續代理可以存取前面代理的輸出
- 監督者可以綜合形成最終回應
- 你能檢視每個代理產出的內容

<img src="../../../translated_images/zh-MO/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic 範圍作為共享記憶體——FileAgent 寫入 `fileContent`，ReportAgent 讀取後寫入 `report`，你的程式碼讀取最終結果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // 來自 FileAgent 的原始檔案數據
String report = scope.readState("report");            // 來自 ReportAgent 的結構化報告
```


**代理監聽器**提供代理執行的監控與除錯。你在示範中看到的逐步輸出即來自掛接每次代理呼叫的 AgentListener：
- **beforeAgentInvocation** - 主管選擇代理時被呼叫，讓您查看選擇了哪個代理及原因
- **afterAgentInvocation** - 代理完成時被呼叫，顯示其結果
- **inheritedBySubagents** - 為 true 時，監聽器會監控階層中所有代理

<img src="../../../translated_images/zh-MO/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners 鉤入執行生命周期 — 監控代理何時啟動、完成或遇到錯誤。*

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

除了 Supervisor 模式外，`langchain4j-agentic` 模組還提供多種強大工作流程模式和功能：

<img src="../../../translated_images/zh-MO/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*五種用於編排代理的工作流程模式 — 從簡單的順序管線到有人審核的工作流程。*

| Pattern | Description | Use Case |
|---------|-------------|----------|
| **Sequential** | 按順序執行代理，輸出流向下一個 | 管線：研究 → 分析 → 報告 |
| **Parallel** | 同時運行多個代理 | 獨立任務：天氣＋新聞＋股票 |
| **Loop** | 重複迭代直至條件達成 | 質量評分：優化直到分數≥0.8 |
| **Conditional** | 根據條件進行路由 | 分類 → 路由到專家代理 |
| **Human-in-the-Loop** | 加入人工審核點 | 審批工作流程、內容審核 |

## 主要概念

現在您已探索 MCP 及 agentic 模組實際運作，我們總結何時使用各種方式。

<img src="../../../translated_images/zh-MO/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 建立通用協議生態系統 — 任何 MCP 相容伺服器均可與任何 MCP 相容客戶端配合，實現跨應用程式的工具共享。*

**MCP** 非常適合用於利用現有工具生態系統、構建多應用共用的工具、用標準協議整合第三方服務，或在不更改程式碼的情況下切換工具實現。

**Agentic 模組** 最適用於您想使用 `@Agent` 註解定義宣告式代理，需要工作流程編排（順序、迴圈、並行），偏好基於介面的代理設計而非命令式程式碼，或組合多個代理共享輸出鍵 `outputKey`。

**Supervisor Agent 模式** 在工作流程不可預測且希望由 LLM 決定時，擁有多個專門代理需要動態編排時，構建路由至不同能力的對話系統時，或想要最靈活、最具適應性的代理行為時尤為突出。

<img src="../../../translated_images/zh-MO/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*何時使用客製 @Tool 方法 versus MCP 工具 — 客製工具適用於應用特定邏輯且具有完整型別安全，MCP 工具則為跨應用的標準化整合。*

## 恭喜！

<img src="../../../translated_images/zh-MO/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*您完成了所有五個模組的學習之旅 — 從基本聊天到 MCP 驅動的 agentic 系統。*

您已完成 LangChain4j 初學者課程。您學會了：

- 如何建立帶記憶的對話式 AI（模組 01）
- 不同任務的提示工程模式（模組 02）
- 用 RAG 方式將回答紮根於您的文件中（模組 03）
- 使用自訂工具創建基礎 AI 代理（助手）（模組 04）
- 使用 LangChain4j MCP 和 Agentic 模組整合標準化工具（模組 05）

### 下一步？

完成模組後，可瀏覽 [Testing Guide](../docs/TESTING.md) 以了解 LangChain4j 測試概念示範。

**官方資源：**
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - 全面指南與 API 參考
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - 原始碼及範例
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - 各種案例的逐步教學

感謝您完成本課程！

---

**導航：** [← 上一節：模組 04 - Tools](../04-tools/README.md) | [返回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件是使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯的。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->