# Module 05: 模型背景協議（MCP）

## 目錄

- [你將學到什麼](../../../05-mcp)
- [什麼是 MCP？](../../../05-mcp)
- [MCP 如何運作](../../../05-mcp)
- [代理模組](../../../05-mcp)
- [執行範例](../../../05-mcp)
  - [先決條件](../../../05-mcp)
- [快速開始](../../../05-mcp)
  - [檔案操作（Stdio）](../../../05-mcp)
  - [監督代理](../../../05-mcp)
    - [執行示範](../../../05-mcp)
    - [監督如何運作](../../../05-mcp)
    - [FileAgent 如何在執行時發現 MCP 工具](../../../05-mcp)
    - [回應策略](../../../05-mcp)
    - [理解輸出](../../../05-mcp)
    - [代理模組功能說明](../../../05-mcp)
- [關鍵概念](../../../05-mcp)
- [恭喜你！](../../../05-mcp)
  - [下一步？](../../../05-mcp)

## 你將學到什麼

你已經建立了會話式 AI、精通提示語、能夠基於文件生成回應，並創建帶有工具的代理。但這些工具都是為你的特定應用程式自訂開發的。如果你能讓你的 AI 存取一個標準化、任何人都能建立並共享的工具生態系統，會如何呢？在本模組中，你將學習怎樣利用模型背景協議（MCP）和 LangChain4j 的代理模組來實現這一點。我們首先展示一個簡單的 MCP 檔案閱讀工具，然後展示它如何輕鬆整合到使用監督代理模式的高級代理流程中。

## 什麼是 MCP？

模型背景協議（MCP）正是提供了這樣一個機制——為 AI 應用提供一種標準方式去發現和使用外部工具。你不再需要針對每個數據源或服務撰寫自訂整合，而是連接到以一致格式公開其功能的 MCP 伺服器。你的 AI 代理便可自動發現並使用這些工具。

下圖展示了差別——沒有 MCP，每個整合都需要自訂點對點連接；有了 MCP，單一協議即可將你的應用連接到任何工具：

<img src="../../../translated_images/zh-MO/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 之前：複雜的點對點整合。MCP 之後：一個協議，無限可能。*

MCP 解決了 AI 開發中的一個根本問題：每個整合都是自訂的。想存取 GitHub？自訂代碼。想讀取檔案？自訂代碼。想查詢資料庫？自訂代碼。這些整合都無法與其他 AI 應用共用。

MCP 標準化了這一過程。MCP 服務端公開工具，配以清晰描述和參數架構。任何 MCP 用戶端都能連接、發現可用工具並使用它們。一次構建，隨處使用。

下圖說明了此架構——單一 MCP 用戶端（你的 AI 應用）連接多個 MCP 服務端，每個服務端透過標準協議公開其工具集：

<img src="../../../translated_images/zh-MO/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型背景協議架構 - 標準化的工具探索與執行*

## MCP 如何運作

在底層，MCP 採用分層架構。你的 Java 應用（MCP 用戶端）發現可用工具，透過傳輸層（Stdio 或 HTTP）發送 JSON-RPC 請求，MCP 服務端執行操作並返回結果。下圖分解此協議的每個層級：

<img src="../../../translated_images/zh-MO/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 底層運作方式——用戶端發現工具，交換 JSON-RPC 訊息，透過傳輸層執行操作。*

**伺服器-用戶端架構**

MCP 採用客戶端-伺服器模型。伺服器提供工具——檔案讀取、資料庫查詢、API 呼叫。用戶端（你的 AI 應用）連接伺服器使用工具。

若想在 LangChain4j 中使用 MCP，請新增此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**工具發現**

當你的用戶端連接 MCP 伺服器時，它會詢問「你有哪些工具？」伺服器返回一份包含描述和參數架構的可用工具列表。你的 AI 代理就可基於用戶請求決定使用哪些工具。下圖展示此握手過程——用戶端發送 `tools/list` 請求，伺服器回傳可用工具及其描述和參數架構：

<img src="../../../translated_images/zh-MO/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在啟動時發現可用工具——現在知道有哪些功能，並可決定使用哪些。*

**傳輸機制**

MCP 支援不同傳輸機制。兩種選項為 Stdio（用於本地子程序通信）和可串流 HTTP（用於遠端伺服器）。本模組示範 Stdio 傳輸：

<img src="../../../translated_images/zh-MO/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 傳輸機制：遠端伺服器用 HTTP，本地進程用 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

適用於本地進程。你的應用自行啟動伺服器作子程序，並透過標準輸入/輸出通訊。適用於檔案系統存取或命令列工具。

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

`@modelcontextprotocol/server-filesystem` 伺服器暴露以下工具，均沙箱至你指定的目錄：

| 工具 | 描述 |
|------|-------------|
| `read_file` | 讀取單一檔案內容 |
| `read_multiple_files` | 一次讀取多個檔案 |
| `write_file` | 建立或覆寫檔案 |
| `edit_file` | 針對性搜尋替換編輯 |
| `list_directory` | 列出路徑下的檔案和目錄 |
| `search_files` | 遞迴搜尋符合模式的檔案 |
| `get_file_info` | 取得檔案元資料（大小、時間戳、權限） |
| `create_directory` | 建立目錄（含父目錄） |
| `move_file` | 移動或重新命名檔案或目錄 |

下圖展示 Stdio 傳輸於執行時的運作方式——你的 Java 應用啟動 MCP 伺服器為子程序，雙方透過 stdin/stdout 管線通信，毋須經過網路或 HTTP：

<img src="../../../translated_images/zh-MO/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 傳輸實況——你的應用啟動 MCP 伺服器子程序，並透過 stdin/stdout 管線通信。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 開啟 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)，並提問：
> - 「Stdio 傳輸如何運作，何時應用 Stdio 而非 HTTP？」
> - 「LangChain4j 如何管理啟動的 MCP 伺服器程序生命週期？」
> - 「讓 AI 存取檔案系統有什麼安全性考慮？」

## 代理模組

MCP 提供標準化工具，LangChain4j 的**代理模組**則提供一個宣告式方式來建立協調這些工具的代理。`@Agent` 註解與 `AgenticServices` 讓你透過介面定義代理行為，而非指令式代碼。

在本模組，你將探索**監督代理**模式——這是一種進階代理式 AI 方法，監督代理根據用戶請求動態決定調用哪些子代理。我們會結合兩個概念，賦予其中一個子代理 MCP 驅動的檔案存取能力。

要使用代理模組，請加入此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **注意：** `langchain4j-agentic` 模組使用獨立的版本屬性（`langchain4j.mcp.version`），因為它與核心 LangChain4j 庫的發行節奏不同。

> **⚠️ 實驗性功能：** `langchain4j-agentic` 模組為**實驗性質**，未來可能變動。構建 AI 助手的穩定方式仍是使用 `langchain4j-core` 配合自訂工具（第 04 模組）。

## 執行範例

### 先決條件

- 完成 [第 04 模組 - 工具](../04-tools/README.md)（本模組建立在自訂工具概念基礎上，並與 MCP 工具作比較）
- 專案根目錄下有含 Azure 憑證的 `.env` 檔（由第 01 模組中的 `azd up` 建立）
- Java 21+，Maven 3.9+
- Node.js 16+ 與 npm（供 MCP 伺服器使用）

> **注意：** 如果尚未設定環境變數，請參考 [第 01 模組 - 介紹](../01-introduction/README.md) 中的部署說明（`azd up` 會自動建立 `.env` 檔），或將 `.env.example` 複製為根目錄的 `.env` 並填入你的值。

## 快速開始

**使用 VS Code：** 只需在瀏覽器中右鍵任何示範檔案，選擇 **「執行 Java」**，或使用「執行與除錯」面板中的啟動組態（確保先配置好 `.env` 中的 Azure 憑證）。

**使用 Maven：** 或可從命令列執行以下範例。

### 檔案操作（Stdio）

示範基於本地子程序的工具。

**✅ 無需先決條件** — MCP 伺服器會自動啟動。

**使用啟動腳本（推薦）：**

啟動腳本會自動從根目錄 `.env` 載入環境變數：

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

**使用 VS Code：** 右鍵 `StdioTransportDemo.java`，選擇 **「執行 Java」**（確保已配置 `.env`）。

應用會自動啟動檔案系統 MCP 伺服器並讀取一個本地檔案。注意如何自動管理子程序。

**預期輸出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### 監督代理

**監督代理模式**是代理式 AI 的**靈活**形式。監督者使用大型語言模型（LLM）自主決定根據用戶請求調用哪些代理。在下一個範例中，我們結合 MCP 驅動的檔案存取和 LLM 代理，建立一個監督的「讀檔案 → 報告」流程。

示範中，`FileAgent` 使用 MCP 檔案系統工具讀取檔案，`ReportAgent` 則生成結構化報告，包括一句執行摘要、三個重點和建議。監督者自動協調這個流程：

<img src="../../../translated_images/zh-MO/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*監督者利用其 LLM 決定調用哪些代理及其順序—無需硬編碼路由。*

下圖展示我們檔案到報告流程的具體工作流程：

<img src="../../../translated_images/zh-MO/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 透過 MCP 工具讀取檔案，然後 ReportAgent 將原始內容轉換為結構化報告。*

下圖序列圖描繪完整監督協調過程：從啟動 MCP 伺服器、監督者自主選擇代理，到通過 stdio 呼叫工具及產生最終報告：

<img src="../../../translated_images/zh-MO/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*監督者自主調用 FileAgent（該代理透過 stdio 呼叫 MCP 伺服器讀檔案），再調用 ReportAgent 生成結構化報告——每個代理將輸出存在共享的 Agentic 範圍。*

每個代理將其輸出存入**Agentic 範圍**（共享記憶體），允許下游代理訪問先前結果。這示範了 MCP 工具如何無縫整合入代理流程——監督者無需知道檔案如何被讀取，只需知道 `FileAgent` 可以勝任此事。

#### 執行示範

啟動腳本會自動從根目錄 `.env` 載入環境變數：

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

**使用 VS Code：** 右鍵 `SupervisorAgentDemo.java`，選擇 **「執行 Java」**（確保已配置 `.env`）。

#### 監督如何運作

在建立代理前，需先連接 MCP 傳輸至用戶端並包裝為 `ToolProvider`。這樣 MCP 伺服器的工具才可供代理使用：

```java
// 從傳輸建立一個 MCP 用戶端
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// 將用戶端包裝為 ToolProvider — 這將 MCP 工具連接到 LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

接著你可將 `mcpToolProvider` 注入任何需要 MCP 工具的代理：

```java
// 第一步：FileAgent 使用 MCP 工具讀取檔案
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 具有用於檔案操作的 MCP 工具
        .build();

// 第二步：ReportAgent 生成結構化報告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor 協調檔案 → 報告流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 返回最終報告
        .build();

// Supervisor 根據請求決定調用哪些代理
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### FileAgent 如何在執行時發現 MCP 工具

你可能會疑惑：**`FileAgent` 如何知道怎麼使用 npm 檔案系統工具？** 答案是它不知道——**LLM** 根據工具架構在執行時推斷。

`FileAgent` 介面只是**提示語定義**。它不含任何 `read_file`、`list_directory` 或其他 MCP 工具的硬編碼知識。完整過程如下：
1. **伺服器啟動：** `StdioMcpTransport` 啟動 `@modelcontextprotocol/server-filesystem` npm 套件作為子程序  
2. **工具發現：** `McpClient` 向伺服器發送 `tools/list` JSON-RPC 請求，伺服器回應包含工具名稱、描述及參數結構（例如 `read_file` — *「讀取檔案的完整內容」* — `{ path: string }`）  
3. **結構注入：** `McpToolProvider` 將這些發現的結構包裝起來，並提供給 LangChain4j 使用  
4. **LLM 決策：** 當呼叫 `FileAgent.readFile(path)` 時，LangChain4j 會將系統訊息、使用者訊息及**工具結構清單**傳送給 LLM。LLM 讀取工具描述並產生工具呼叫（例如 `read_file(path="/some/file.txt")`）  
5. **執行：** LangChain4j 攔截工具呼叫，透過 MCP 用戶端路由回 Node.js 子程序，取得結果後回饋給 LLM  

這與上文描述的 [工具發現](../../../05-mcp) 機制相同，但特別應用於代理流程。`@SystemMessage` 與 `@UserMessage` 標註用來指導 LLM 的行為，而注入的 `ToolProvider` 則賦予它**能力**— LLM 在執行時橋接兩者。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試看：** 打開 [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) 並問：  
> - 「這個代理怎麼知道呼叫哪個 MCP 工具？」  
> - 「如果我從代理建構器移除 ToolProvider，會發生什麼？」  
> - 「工具結構是如何傳遞給 LLM 的？」  

#### 回應策略

配置 `SupervisorAgent` 時，您會指定子代理完成任務後，如何形成對使用者的最終回答。下圖展示三種可用策略 — LAST 直接回傳最終代理的輸出，SUMMARY 透過 LLM 綜整所有輸出，SCORED 則選擇針對原始請求得分較高的回應：

<img src="../../../translated_images/zh-MO/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*三種 Supervisor 形成最終回應的策略 — 根據是否需要最後代理的輸出、綜合摘要或最高得分選擇。*

可用策略說明：

| 策略 | 說明 |
|----------|-------------|
| **LAST** | 監督者回傳最後一個被呼叫子代理或工具的輸出。適用於工作流中最後一個代理專門產生完整最終答案的情況（例如研究流程中的「摘要代理」）。 |
| **SUMMARY** | 監督者使用內部的語言模型（LLM）綜合整個互動及所有子代理輸出，並回傳該摘要作為最終回應。可提供乾淨、聚合的回答給使用者。 |
| **SCORED** | 系統用內部 LLM 針對 LAST 回應與整體摘要分別評分，針對原始使用者請求選擇得分較高的輸出。 |

完整實作請參考 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試看：** 打開 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 並問：  
> - 「監督者怎麼決定要呼叫哪些代理？」  
> - 「Supervisor 和 Sequential 工作流模式有何不同？」  
> - 「我怎麼自訂 Supervisor 的規劃行為？」  

#### 理解輸出結果

執行示範時，您會看到 Supervisor 如何協調多代理的有結構流程。以下說明各部分意義：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**標題**介紹工作流程概念：從讀取檔案到產生報告的專注管線。  

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
  
**工作流程圖**顯示代理間資料流。每個代理扮演特定角色：  
- **FileAgent** 使用 MCP 工具讀取檔案，將原始內容存到 `fileContent`  
- **ReportAgent** 取用該內容並產生結構化報告存於 `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**使用者請求**顯示任務。Supervisor 解析請求並決定調用 FileAgent → ReportAgent。  

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
  
**監督者協調**展示兩步驟流程：  
1. **FileAgent** 利用 MCP 讀取檔案並存取內容  
2. **ReportAgent** 接收內容並生成結構化報告  

監督者基於使用者請求**自動**做此決策。

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
  
#### Agentic 模組特性解說

範例展示多項進階 agentic 模組功能，讓我們細看 Agentic Scope 與 Agent Listeners。

**Agentic Scope** 顯示代理使用 `@Agent(outputKey="...")` 存放結果的共享記憶體。這讓：  
- 後續代理能訪問前面代理的輸出  
- 監督者可綜合形成最終回答  
- 您能檢查各代理產出  

下圖展示 Agentic Scope 在檔案到報告工作流中作為共享記憶體的運作— FileAgent 輸出寫入 `fileContent`，ReportAgent 讀取並寫入 `report`：

<img src="../../../translated_images/zh-MO/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope 作為共享記憶體— FileAgent 寫入 `fileContent`，ReportAgent 讀取並寫入 `report`，您的程式讀取最終結果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // 來自FileAgent的原始檔案資料
String report = scope.readState("report");            // 來自ReportAgent的結構化報告
```
  
**Agent Listeners** 允許監控和除錯代理執行。示範中逐步輸出來自一個掛鉤於每次代理呼叫的 AgentListener：  
- **beforeAgentInvocation** — 監督者選擇代理時調用，讓您看到被選中代理及原因  
- **afterAgentInvocation** — 代理完成後調用，顯示其結果  
- **inheritedBySubagents** — 若為 true，該監聽器監控階層中所有代理  

下圖顯示完整 Agent Listener 生命週期，包含 `onError` 如何處理代理執行時的錯誤：

<img src="../../../translated_images/zh-MO/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners 挂钩于执行生命周期—监控代理开始、完成或发生错误。*

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
        return true; // 傳播至所有子代理
    }
};
```
  
除了 Supervisor 模式，`langchain4j-agentic` 模組還提供多種強大工作流模式。下圖顯示五種模式—從簡單依序管線到人類介入審核工作流：

<img src="../../../translated_images/zh-MO/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*五種代理協調工作流模式—從簡單依序到人類介入審核。*

| 模式 | 說明 | 使用案例 |
|---------|-------------|----------|
| **Sequential** | 依序執行代理，輸出傳給下一個 | 管線：研究 → 分析 → 報告 |
| **Parallel** | 同時執行多代理 | 獨立任務：天氣＋新聞＋股票 |
| **Loop** | 反覆執行直到條件達成 | 品質評分：精煉直到分數 ≥ 0.8 |
| **Conditional** | 根據條件路由 | 分類 → 導向專家代理 |
| **Human-in-the-Loop** | 新增人類檢查點 | 審核工作流、內容審查 |

## 主要概念

您已探索 MCP 及 agentic 模組的實作，現在總結何時使用各種方式。

MCP 最大優勢之一是其不斷成長的生態系。下圖展示一個通用協定如何串接多種 MCP 伺服器—從檔案系統與資料庫存取到 GitHub、電子郵件、網頁爬蟲等等：

<img src="../../../translated_images/zh-MO/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 建立通用協定生態系—任何 MCP 相容伺服器皆能與 MCP 客戶端配合，讓工具跨應用共享。*

**MCP** 適合想使用既有工具生態系、建立可共用的標準化工具、整合第三方服務或靈活更換工具實作而不改程式碼時。

**Agentic 模組** 適合想用聲明式代理定義（`@Agent` 標註）、需求工作流協調（序列、迴圈、並行）、偏好介面導向代理設計而非命令式程式碼、或多代理共享輸出（透過 `outputKey`）時。

**Supervisor Agent 模式** 適合流程無法預先確定且由 LLM 動態決定、多專精代理須動態調度、建立可導向不同能力的對話系統，或尋求最高彈性與適應性的代理行為。

以下對比協助您在自訂 `@Tool` 方法（第四章）與 MCP 工具之間選擇，彰顯關鍵權衡—自訂工具提供緊密綁定與完整型別安全，MCP 工具提供標準化且可重用的整合：

<img src="../../../translated_images/zh-MO/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*何時使用自訂 @Tool 方法 vs MCP 工具 — 自訂工具針對應用程式邏輯提供型別安全，MCP 工具則為跨應用標準化整合。*

## 恭喜！

您已完成 LangChain4j 初學者課程的全部五個模組！以下是您完成的完整學習旅程—從基本聊天到 MCP 支援的 agentic 系統：

<img src="../../../translated_images/zh-MO/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*您透過全部五個模組的學習之旅—從基本聊天到 MCP 支援的 agentic 系統。*

您學會了：

- 如何打造帶有記憶功能的對話 AI（模組 01）  
- 不同任務的提示工程模式（模組 02）  
- 利用 RAG 根據文件生成回答（模組 03）  
- 用自訂工具創建基本 AI 代理（助手）（模組 04）  
- 透過 LangChain4j MCP 及 Agentic 模組整合標準化工具（模組 05）  

### 下一步？

完成模組後，探索 [測試指南](../docs/TESTING.md) 了解 LangChain4j 的測試概念實作。

**官方資源：**  
- [LangChain4j 文件](https://docs.langchain4j.dev/) — 全面指南與 API 參考  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) — 程式碼與範例  
- [LangChain4j 教學](https://docs.langchain4j.dev/tutorials/) — 各種用例的分步教學  

感謝您完成本課程！

---

**導覽：** [← 上一章：模組 04 - 工具](../04-tools/README.md) | [回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件是使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。儘管我們努力追求準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始語言版本的文件應被視為權威來源。對於重要資訊，建議使用專業人工翻譯。對於因使用本翻譯而產生的任何誤解或誤釋，我們概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->