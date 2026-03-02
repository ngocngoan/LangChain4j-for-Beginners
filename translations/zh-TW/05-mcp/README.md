# Module 05: 模型上下文協定 (MCP)

## 目錄

- [你將學到的內容](../../../05-mcp)
- [什麼是 MCP？](../../../05-mcp)
- [MCP 如何運作](../../../05-mcp)
- [Agentic 模組](../../../05-mcp)
- [執行範例](../../../05-mcp)
  - [先決條件](../../../05-mcp)
- [快速開始](../../../05-mcp)
  - [檔案操作（Stdio）](../../../05-mcp)
  - [監督代理](../../../05-mcp)
    - [執行示範](../../../05-mcp)
    - [監督者運作原理](../../../05-mcp)
    - [回應策略](../../../05-mcp)
    - [理解輸出](../../../05-mcp)
    - [Agentic 模組功能說明](../../../05-mcp)
- [關鍵概念](../../../05-mcp)
- [恭喜！](../../../05-mcp)
  - [接下來呢？](../../../05-mcp)

## 你將學到的內容

你已經建立了對話式 AI、精通提示詞、將回應依據文件且建立具備工具的代理。但這些工具都是為你的特定應用程式量身打造。假如你能讓 AI 訪問一個任何人都可以建立並共享的標準化工具生態系呢？在本單元中，你將學習如何透過模型上下文協定 (MCP) 和 LangChain4j 的 agentic 模組來做到這點。我們首先展示一個簡單的 MCP 檔案閱讀器，然後示範它如何輕鬆整合到使用監督代理模式的先進 agentic 工作流程中。

## 什麼是 MCP？

模型上下文協定（MCP）正是為此而生——為 AI 應用程序提供一種標準方式來發現和使用外部工具。你不需要為每個資料源或服務寫自訂整合，改為連接到以統一格式公開其功能的 MCP 伺服器。你的 AI 代理便可自動發現並使用這些工具。

下圖顯示了差異——沒有 MCP，每個整合都需要自訂點對點連接；有了 MCP，單一協定便連接你的應用至任何工具：

<img src="../../../translated_images/zh-TW/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 之前：複雜的點對點整合。MCP 之後：一個協定，無限可能。*

MCP 解決了 AI 開發中的基本問題：每個整合都是自訂的。想訪問 GitHub？自訂程式碼。想讀取檔案？自訂程式碼。想查詢資料庫？自訂程式碼。且這些整合都無法與其他 AI 應用兼容。

MCP 將此標準化。MCP 伺服器公開帶有明確描述和結構的工具，任何 MCP 客戶端都可連接、發現可用工具並使用它們。一次構建，處處可用。

下圖展示了此架構——單一 MCP 客戶端（你的 AI 應用）連接多個 MCP 伺服器，每個伺服器透過標準協定公開它們自己的工具集：

<img src="../../../translated_images/zh-TW/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型上下文協定架構 — 標準化工具發現與執行*

## MCP 如何運作

在內部，MCP 採用分層架構。你的 Java 應用（MCP 客戶端）發現可用工具，透過傳輸層（Stdio 或 HTTP）發送 JSON-RPC 請求，MCP 伺服器執行操作並回傳結果。以下圖解構了此協定中每個層級：

<img src="../../../translated_images/zh-TW/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 如何在內部運作 — 客戶端發現工具，交換 JSON-RPC 訊息，並透過傳輸層執行操作。*

**伺服器-客戶端架構**

MCP 採用客戶端-伺服器模型。伺服器提供工具——讀取檔案、查詢資料庫、呼叫 API。客戶端（你的 AI 應用）連接伺服器並使用其工具。

要在 LangChain4j 中使用 MCP，加入此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**工具發現**

當客戶端連接到 MCP 伺服器時，它會詢問「你有哪些工具？」伺服器回傳可用工具列表，包含描述和參數結構。你的 AI 代理便可根據使用者需求決定使用哪些工具。下圖展示此握手過程 —— 客戶端發送 `tools/list` 請求，伺服器回傳包含描述與參數結構的工具清單：

<img src="../../../translated_images/zh-TW/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在啟動時發現可用工具——現在它知道有哪些功能可用，可以決定使用哪些工具。*

**傳輸機制**

MCP 支援不同的傳輸機制。兩種選擇為 Stdio（用於本地子進程通訊）與可串流 HTTP（用於遠端伺服器）。本單元演示 Stdio 傳輸：

<img src="../../../translated_images/zh-TW/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 傳輸機制：遠端伺服器用 HTTP，本地進程用 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

用於本地進程。你的應用會啟動伺服器子程序，並透過標準輸入/輸出溝通。適合檔案系統存取或命令列工具。

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

`@modelcontextprotocol/server-filesystem` 伺服器公開以下工具，均受你指定目錄沙箱限制：

| 工具 | 說明 |
|------|-------------|
| `read_file` | 讀取單一檔案內容 |
| `read_multiple_files` | 一次讀取多個檔案 |
| `write_file` | 建立或覆寫檔案 |
| `edit_file` | 進行指定的尋找並取代編輯 |
| `list_directory` | 列出路徑下的檔案與目錄 |
| `search_files` | 遞迴搜尋符合模式的檔案 |
| `get_file_info` | 取得檔案元資料（大小、時間戳、權限） |
| `create_directory` | 建立目錄（包含父目錄） |
| `move_file` | 移動或重新命名檔案或目錄 |

下圖展示 Stdio 傳輸在執行期間的運作方式——你的 Java 應用啟動 MCP 伺服器作為子程序，兩者通過 stdin/stdout 管線通訊，無需網路或 HTTP 參與：

<img src="../../../translated_images/zh-TW/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 傳輸現場 — 你的應用啟動 MCP 伺服器作為子程序，並透過 stdin/stdout 管線通訊。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 開啟 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)，問：
> - 「Stdio 傳輸如何運作？什麼時候應該用它而不是 HTTP？」
> - 「LangChain4j 如何管理啟動的 MCP 伺服器進程生命週期？」
> - 「讓 AI 存取檔案系統有哪些安全性考量？」

## Agentic 模組

MCP 提供標準化工具，LangChain4j 的 **agentic 模組** 則提供一種宣告式方式來建構能協調這些工具的代理。`@Agent` 註解和 `AgenticServices` 讓你透過介面定義代理行為，而非命令式程式碼。

在本單元，你將探索 **監督代理** 模式—一種進階 agentic AI 方法，其中「監督者」代理根據使用者請求動態決定調用哪些子代理。我們會結合兩者，讓其中一個子代理具備 MCP 動力的檔案存取能力。

要使用 agentic 模組，加入這個 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **注意：** `langchain4j-agentic` 模組使用獨立版本屬性（`langchain4j.mcp.version`），因為它與核心 LangChain4j 函式庫發布節奏不同。

> **⚠️ 實驗性功能：** `langchain4j-agentic` 模組為 **實驗性**，未來可能變動。建構 AI 助手的穩定方式仍是使用 `langchain4j-core` 加自訂工具（第四單元）。

## 執行範例

### 先決條件

- 完成 [第四單元 - 工具](../04-tools/README.md)（本單元建立在自訂工具概念上，並與 MCP 工具比較）
- 根目錄有 `.env` 檔，包含 Azure 憑證（由第一單元中 `azd up` 建立）
- Java 21+、Maven 3.9+
- Node.js 16+ 和 npm（用於 MCP 伺服器）

> **注意：** 若尚未設定環境變數，請參閱 [第一單元 - 介紹](../01-introduction/README.md) 取得部署指引（`azd up` 會自動建立 `.env`），或將 `.env.example` 複製成根目錄下 `.env` 並填入你的數值。

## 快速開始

**使用 VS Code：** 只需在檔案總管中對任何示範檔案右鍵點擊並選擇 **「執行 Java」**，或使用「執行與偵錯」面板中的啟動組態（請先確保 `.env` 檔配置完善）。

**使用 Maven：** 你也可以使用下列範例從命令列執行。

### 檔案操作（Stdio）

演示基於本地子程序的工具。

**✅ 無需先決條件** - MCP 伺服器會自動啟動。

**使用啟動腳本（推薦）：**

啟動腳本會自動從根目錄的 `.env` 檔載入環境變數：

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

**使用 VS Code：** 對 `StdioTransportDemo.java` 右鍵，選擇 **「執行 Java」**（請確保 `.env` 已設定）。

應用程式自動啟動檔案系統 MCP 伺服器並讀取本地檔案。注意子程序管理已由系統代勞。

**預期輸出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### 監督代理

**監督代理模式**是一種**彈性**的 agentic AI 形式。監督者使用大型語言模型（LLM）自主決定根據使用者請求該呼叫哪些代理。在下一個範例中，我們結合 MCP 動力的檔案存取與 LLM 代理，建立一個監督的檔案讀取 → 報告流程。

示範中，`FileAgent` 使用 MCP 檔案系統工具讀取檔案，`ReportAgent` 產生結構化報告，包含一段執行摘要（1 句）、三個要點及建議。監督者自動協調此流程：

<img src="../../../translated_images/zh-TW/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*監督者使用其 LLM 決定呼叫哪些代理以及呼叫順序—無須硬編碼路由。*

以下是我們檔案轉報告流程的具體執行步驟：

<img src="../../../translated_images/zh-TW/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 透過 MCP 工具讀取檔案，然後 ReportAgent 將原始內容轉成結構化報告。*

每個代理將其輸出存入 **Agentic 範圍**（共享記憶體），讓後續代理能存取前面的結果。這示範了 MCP 工具如何無縫整合進 agentic 工作流程——監督者不需知道 *檔案怎麼讀*，只需知道 `FileAgent` 能做到。

#### 執行示範

啟動腳本會自動從根目錄的 `.env` 載入環境變數：

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

**使用 VS Code：** 對 `SupervisorAgentDemo.java` 右鍵，選擇 **「執行 Java」**（請確保 `.env` 已設定）。

#### 監督者運作原理

建構代理前，你需要連接 MCP 傳輸到客戶端並將其包裝成 `ToolProvider`。這是讓 MCP 伺服器工具能用於你的代理的方法：

```java
// 從傳輸建立 MCP 用戶端
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// 將用戶端包裝為 ToolProvider — 這將 MCP 工具橋接到 LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

現在你可以將 `mcpToolProvider` 注入到任何需要 MCP 工具的代理：

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

// Supervisor 協調檔案 → 報告的工作流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 返回最終報告
        .build();

// Supervisor 根據請求決定要呼叫哪些代理人
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### 回應策略

配置 `SupervisorAgent` 時，你要指定子代理完成任務後，如何向使用者組織最終回答。下圖展示三種可用策略——LAST 直接回傳最後一個代理輸出、SUMMARY 透過 LLM 綜整所有輸出、SCORED 根據原始請求對兩者進行評分挑選較佳回應：

<img src="../../../translated_images/zh-TW/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*監督者組織最終回應的三種策略——根據你想要最後代理輸出、綜合摘要或最高分選項來選擇。*

可用策略說明如下：

| 策略 | 說明 |
|----------|-------------|
| **LAST** | 監督者回傳最後呼叫的子代理或工具的輸出。當流程中最後一個代理特別設計用於產生完整最終答案時，此策略很有用（例如研究流程中的「摘要代理」）。 |
| **SUMMARY** | 監督者使用內部大型語言模型（LLM）綜整整個互動及所有子代理輸出成摘要，然後回傳這個摘要作為最終回答。這為使用者提供清晰、彙整的答案。 |
| **SCORED** | 系統使用內部 LLM 對 LAST 回應與互動摘要兩者基於原始使用者請求進行評分，回傳分數較高的輸出。 |
完整實作請參考 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天體驗：** 開啟 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 並提問：
> - 「Supervisor 如何決定要調用哪些 agents？」
> - 「Supervisor 與 Sequential 工作流程模式有何差異？」
> - 「我該如何自訂 Supervisor 的規劃行為？」

#### 理解輸出內容

運行範例時，你會看到 Supervisor 如何協調多個 agents 的結構化流程。下面說明每個區段的意義：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**標題**介紹了工作流程的概念：從檔案讀取到報告生成的聚焦管線。

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

**工作流程圖**展示 agents 間的資料流。每個 agent 扮演特定角色：
- **FileAgent** 使用 MCP 工具讀取檔案並將原始內容儲存在 `fileContent`
- **ReportAgent** 使用該內容產生結構化報告於 `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**使用者請求**示範了任務。Supervisor 解析該請求決定按序調用 FileAgent → ReportAgent。

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

**Supervisor 協調**顯示該兩步流程的實際運作：
1. **FileAgent** 透過 MCP 讀取檔案並儲存內容
2. **ReportAgent** 接收內容並產生結構化報告

Supervisor 是根據使用者請求**自主決策**的。

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

範例展示了 agentic 模組的多項進階功能。以下詳解 Agentic Scope 和 Agent Listeners。

**Agentic Scope** 展示 agents 使用 `@Agent(outputKey="...")` 共享記憶的機制。它允許：
- 後續 agents 存取先前 agents 的輸出
- Supervisor 整合生成最終回應
- 你檢視每個 agent 的產出結果

下圖說明 Agentic Scope 如何作為檔案至報告工作流程的共享記憶 —— FileAgent 將輸出寫入 `fileContent`，ReportAgent 讀取該輸入並寫入 `report`：

<img src="../../../translated_images/zh-TW/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope 作為共享記憶 — FileAgent 寫入 `fileContent`，ReportAgent 讀取並寫入 `report`，你的程式碼讀取最終結果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent 的原始檔案數據
String report = scope.readState("report");            // ReportAgent 的結構化報告
```

**Agent Listeners** 支援執行監控與除錯。你在範例看到的逐步輸出來自一個掛載於每次 agent 調用的 AgentListener：
- **beforeAgentInvocation** - 當 Supervisor 選擇 agent 時呼叫，讓你知道被選中 agent 與原因
- **afterAgentInvocation** - agent 執行完成時呼叫，顯示結果
- **inheritedBySubagents** - 設為 true 時，監控所有階層中的 agents

下圖顯示完整的 Agent Listener 生命週期，包括 `onError` 如何處理 agent 執行錯誤：

<img src="../../../translated_images/zh-TW/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners 掛載於執行生命週期 — 監視 agents 啟動、完成或錯誤狀態。*

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
        return true; // 傳播給所有子代理
    }
};
```

除了 Supervisor 模式外，`langchain4j-agentic` 模組還提供多種強大工作流程模式。下圖顯示五種模式——從簡單序列管線至人類介入審批流程：

<img src="../../../translated_images/zh-TW/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*五種 orchestrating agents 的工作流程模式——從簡單序列管線至人類介入審批流程。*

| 模式 | 說明 | 使用案例 |
|---------|-------------|----------|
| **Sequential** | 依序執行 agents，輸出流入下一步 | 管線：研究 → 分析 → 報告 |
| **Parallel** | 同時並行執行 agents | 獨立任務：天氣 + 新聞 + 股票 |
| **Loop** | 迭代直到達成條件 | 品質評分：反覆調整直到分數 ≥ 0.8 |
| **Conditional** | 根據條件選擇路徑 | 分類 → 導向專家 agent |
| **Human-in-the-Loop** | 加入人為審核點 | 審批流程、內容審閱 |

## 關鍵概念

了解 MCP 與 agentic 模組實作後，我們來總結何時應用各方案。

MCP 最大優勢之一是其不斷擴展的生態系統。下圖展示透過單一通用協定，AI 應用可以連接到多種 MCP 伺服器——涵蓋檔案系統、資料庫、GitHub、郵件、網頁爬蟲等：

<img src="../../../translated_images/zh-TW/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 建立通用協定生態系——任何 MCP 兼容伺服器皆可被任何 MCP 兼容客戶端使用，實現工具跨應用共享。*

**MCP** 適合想要利用現有工具生態、建立多應用共享的工具、透過標準協定整合第三方服務、或能不改程式碼切換工具實作的場景。

**Agentic 模組** 適合需要以 `@Agent` 註解宣告式定義 agents、需要工作流程協調（序列、迴圈、並行）、偏好介面式 agent 設計而非命令式程式碼、或多個 agents 共享輸出（利用 `outputKey`）的需求。

**Supervisor Agent 模式** 在工作流程無法事先明確、需 LLM 根據情境動態決策、有多個專門化 agents 需靈活調度、建立導向不同能力的對話系統，或追求最靈活適應力代理行為時最為出色。

為幫助你在自訂 Module 04 的 `@Tool` 方法與本模組 MCP 工具間取捨，以下比較突顯主要考量——自訂工具提供緊密結合與全類型安全的應用特定邏輯，MCP 工具則提供標準化、可重用的整合方案：

<img src="../../../translated_images/zh-TW/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*何時使用自訂 @Tool 方法 vs MCP 工具——自訂工具適合應用特定邏輯且具全類型安全，MCP 工具適合標準化整合且跨應用通用。*

## 恭喜！

你已完成 LangChain4j 初學者完整課程五個模組！回顧你完成的整個學習旅程——從基本聊天到 MCP 驅動的 agentic 系統：

<img src="../../../translated_images/zh-TW/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*你完整的學習旅程涵蓋五個模組——從基礎聊天到 MCP 驅動 agentic 系統。*

你已學會：

- 如何構建具記憶功能的對話式 AI（模組 01）
- 不同任務的提示工程模式（模組 02）
- 使用 RAG 將回應以文件為依據（模組 03）
- 創建帶自訂工具的基礎 AI agents（模組 04）
- 使用 LangChain4j MCP 和 Agentic 模組整合標準化工具（模組 05）

### 接下來呢？

完成本課程後，可以參考 [測試指南](../docs/TESTING.md) 以見識 LangChain4j 的測試概念實作。

**官方資源：**
- [LangChain4j 文件](https://docs.langchain4j.dev/) - 詳盡指南及 API 參考
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - 原始碼與範例
- [LangChain4j 教學](https://docs.langchain4j.dev/tutorials/) - 各種使用案例的逐步教學

感謝你完成此課程！

---

**導覽：** [← 上一篇：模組 04 - 工具](../04-tools/README.md) | [回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。若涉及重要資訊，建議採用專業人工翻譯。本公司對於因使用本翻譯所產生之任何誤解或誤譯不承擔任何責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->