# Module 05: Model Context Protocol (MCP)

## Table of Contents

- [你將學到什麼](../../../05-mcp)
- [什麼是 MCP？](../../../05-mcp)
- [MCP 如何運作](../../../05-mcp)
- [Agentic 模組](../../../05-mcp)
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
    - [Agentic 模組功能說明](../../../05-mcp)
- [關鍵概念](../../../05-mcp)
- [恭喜！](../../../05-mcp)
  - [接下來是什麼？](../../../05-mcp)

## 你將學到什麼

你已經建立了會話式 AI，精通提示語句，能夠以文件為依據生成回應，並利用工具創建了代理。但這些工具都是為你特定的應用客製化打造。如果你可以讓你的 AI 使用一個標準化的工具生態系統，任何人都可以創建和分享這些工具，那會怎樣？在本模組中，你將學習如何使用 Model Context Protocol (MCP) 和 LangChain4j 的 agentic 模組做到這一點。我們先展示一個簡單的 MCP 檔案讀取器，然後示範如何利用監督代理模式輕鬆整合進高度複雜的 agentic 工作流程。

## 什麼是 MCP？

Model Context Protocol (MCP) 正是提供了這樣的功能──為 AI 應用發現和使用外部工具提供一套標準方法。你不需要為每個資料來源或服務編寫客製化整合，而是連接到以一致格式揭露其功能的 MCP 伺服器。你的 AI 代理隨後可以自動發現並使用這些工具。

下圖說明了差異──沒有 MCP 時，每個整合都需要客製化點對點連線；有了 MCP，單一協定即可將你的應用程式連接任何工具：

<img src="../../../translated_images/zh-HK/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 之前：複雜的點對點整合。MCP 之後：一套協定，無限可能。*

MCP 解決了 AI 開發中的基本問題：所有整合都是客製化。想存取 GitHub？自訂程式碼。想讀取檔案？自訂程式碼。想查詢資料庫？自訂程式碼。且這些整合之間彼此無法共用。

MCP 將此標準化。一個 MCP 伺服器暴露工具，並提供清晰的說明和參數結構。任何 MCP 客戶端都能連接、發現可用工具並使用它們。一次建置，處處可用。

下圖示意此架構──單一 MCP 客戶端（你的 AI 應用）連接多個 MCP 伺服器，各自透過標準協定暴露自己的一組工具：

<img src="../../../translated_images/zh-HK/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol 架構──標準化的工具發現與執行*

## MCP 如何運作

MCP 使用分層架構。你的 Java 應用（MCP 客戶端）發現可用工具，透過傳輸層（Stdio 或 HTTP）發送 JSON-RPC 請求，MCP 伺服器執行作業並回傳結果。下圖分解該協定的各層：

<img src="../../../translated_images/zh-HK/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 背後的運作原理——客戶端發現工具、交換 JSON-RPC 訊息，透過傳輸層執行作業。*

**伺服器-客戶端架構**

MCP 採用客戶端伺服器模型。伺服器提供工具──讀取檔案、查詢資料庫、呼叫 API。客戶端（你的 AI 應用）連接這些伺服器並使用其工具。

要在 LangChain4j 中使用 MCP，加入以下 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**工具發現**

當你的客戶端連接到 MCP 伺服器時，會詢問「你有哪些工具？」伺服器會回應可用工具清單，每個工具都有說明和參數結構。你的 AI 代理可以根據用戶需求決定要使用哪些工具。下圖示範此交換──客戶端發送 `tools/list` 請求，伺服器回傳其可用工具以及說明和參數結構：

<img src="../../../translated_images/zh-HK/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在啟動時發現可用工具——它現在知道哪些功能可用，並可決定使用哪些。*

**傳輸機制**

MCP 支援多種傳輸機制。兩種選項是 Stdio（用於本地子程序通訊）和可串流 HTTP（用於遠端伺服器）。本模組示範 Stdio 傳輸：

<img src="../../../translated_images/zh-HK/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 傳輸機制：遠端伺服器用 HTTP，本地程序用 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

適用於本地程序。你的應用程式以子程序形式啟動伺服器，透過標準輸入/輸出通訊。適合檔案系統存取或命令行工具。

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

`@modelcontextprotocol/server-filesystem` 伺服器暴露以下工具，且全部受限於你指定的目錄：

| 工具 | 說明 |
|------|-------------|
| `read_file` | 讀取單一檔案內容 |
| `read_multiple_files` | 一次讀取多個檔案 |
| `write_file` | 建立或覆寫檔案 |
| `edit_file` | 有針對性地查找並替換 |
| `list_directory` | 列出路徑下的檔案和目錄 |
| `search_files` | 遞迴搜尋符合模式的檔案 |
| `get_file_info` | 取得檔案元資料（大小、時間戳記、權限） |
| `create_directory` | 建立目錄（包含父目錄） |
| `move_file` | 移動或重新命名檔案或目錄 |

下圖說明 Stdio 傳輸於執行時的工作流程——你的 Java 應用啟動 MCP 伺服器作為子程序，雙方透過 stdin/stdout 管道通訊，無需網路或 HTTP：

<img src="../../../translated_images/zh-HK/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 傳輸實況——你的應用啟動 MCP 伺服器作為子程序，並透過 stdin/stdout 管道通訊。*

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat:** 開啟 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) 並詢問：
> - 「Stdio 傳輸如何運作？我該何時用它、何時用 HTTP？」
> - 「LangChain4j 如何管理啟動的 MCP 伺服器程序生命週期？」
> - 「讓 AI 存取檔案系統有哪些安全性考量？」

## Agentic 模組

MCP 提供標準化工具，而 LangChain4j 的 **agentic 模組** 提供一種宣告式方法來建構代理，協調這些工具的運用。`@Agent` 註解和 `AgenticServices` 讓你通過介面定義代理行為，而非寫指令式程式碼。

在本模組內，你將探索 **監督代理（Supervisor Agent）** 模式──一種進階的 agentic AI 方法，監督代理根據用戶請求動態決定要呼叫哪些子代理。我們將結合兩個概念，讓其中一個子代理具備 MCP 動力的檔案存取能力。

要使用 agentic 模組，加入此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **注意：** `langchain4j-agentic` 模組採用獨立版本屬性 (`langchain4j.mcp.version`)，因其發布節奏與核心 LangChain4j 庫不同。

> **⚠️ 實驗性：** `langchain4j-agentic` 模組為 **實驗性**，可能會改變。構建 AI 助理的穩定方式仍是使用帶有自訂工具的 `langchain4j-core`（模組 04）。

## 執行範例

### 先決條件

- 完成 [模組 04 - 工具](../04-tools/README.md)（本模組建立在自訂工具概念上並與 MCP 工具做比較）
- 根目錄內有含 Azure 認證的 `.env` 檔（由模組 01 的 `azd up` 建立）
- Java 21+、Maven 3.9+
- Node.js 16+ 和 npm（用於 MCP 伺服器）

> **注意：** 若尚未設定環境變數，請參閱[模組 01 - 介紹](../01-introduction/README.md)的部署指引（`azd up` 會自動建立 `.env` 檔），或複製 `.env.example` 為根目錄的 `.env` 並填入你的值。

## 快速開始

**使用 VS Code：** 只要在檔案總管內對任意示範檔案按右鍵，選擇 **「執行 Java」**，或使用「執行與除錯」面板的啟動設定（請先確保 `.env` 檔含 Azure 認證）。

**使用 Maven：** 你也可以從命令列執行下面的範例。

### 檔案操作（Stdio）

示範基於本地子程序的工具。

**✅ 無需先決條件** — MCP 伺服器會自動啟動。

**使用啟動腳本（推薦）：**

啟動腳本會自動從根目錄的 `.env` 檔載入環境變數：

**Bash：**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell：**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**使用 VS Code：** 對 `StdioTransportDemo.java` 按右鍵並選擇 **「執行 Java」**（請確保 `.env` 檔已配置）。

應用會自動啟動檔案系統 MCP 伺服器並讀取本地檔案。注意子程序管理已為你處理。

**預期輸出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### 監督代理

**監督代理模式** 是 agentic AI 的一種 **靈活** 形式。監督者使用大型語言模型 (LLM) 自主決定依用戶請求呼叫哪些代理。下一個範例，我們將 MCP 強化的檔案存取功能和 LLM 代理結合，創建一個「檔案讀取 → 報告生成」的監督流程。

示範中，`FileAgent` 使用 MCP 檔案系統工具閱讀檔案，`ReportAgent` 則生成結構化報告，包含一段執行摘要（1 句話）、3 個重點及建議。監督者自動協調此流程：

<img src="../../../translated_images/zh-HK/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*監督者利用其 LLM 決定呼叫哪些代理、依何順序──不需預寫路由。*

以下是我們檔案到報告流程的具體工作流：

<img src="../../../translated_images/zh-HK/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 透過 MCP 工具讀檔，ReportAgent 將原始內容轉換為結構化報告。*

下方序列圖追蹤完整的監督者協調過程——從啟動 MCP 伺服器，到監督者自主選擇代理，再到透過 stdio 呼叫工具與最後生成報告：

<img src="../../../translated_images/zh-HK/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*監督者自主呼叫 FileAgent（它透過 stdio 呼叫 MCP 伺服器讀檔），再呼叫 ReportAgent 生成結構化報告——每個代理將輸出儲存在共享的 Agentic Scope。*

每個代理將輸出存放在 **Agentic Scope**（共享記憶體）中，讓下游代理可以讀取之前結果。這展示了 MCP 工具如何無縫融合 agentic 工作流程——監督者不需要知道 *檔案怎麼被讀*，只要知道 `FileAgent` 能做到。

#### 執行示範

啟動腳本會自動從根目錄的 `.env` 檔載入環境變數：

**Bash：**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell：**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**使用 VS Code：** 對 `SupervisorAgentDemo.java` 按右鍵並選擇 **「執行 Java」**（請確保 `.env` 檔已配置）。

#### 監督如何運作

建立代理前，你需要將 MCP 傳輸連接到客戶端並包裝成 `ToolProvider`。這樣 MCP 伺服器的工具才會提供給代理使用：

```java
// 從傳輸建立一個MCP客戶端
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// 將客戶端包裝為ToolProvider — 呢個係將MCP工具橋接到LangChain4j嘅方法
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

現在你可以將 `mcpToolProvider` 注入任何需要 MCP 工具的代理：

```java
// 步驟 1：FileAgent 使用 MCP 工具讀取檔案
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 具備用於檔案操作的 MCP 工具
        .build();

// 步驟 2：ReportAgent 生成結構化報告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor 統籌檔案 → 報告的工作流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 返回最終報告
        .build();

// Supervisor 根據請求決定調用哪些代理程式
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### FileAgent 如何在執行時發現 MCP 工具

你可能會好奇：**`FileAgent` 怎麼知道要如何使用 npm 檔案系統工具？** 答案是它不需要知道 —— **LLM** 會透過工具結構在執行時理解。

`FileAgent` 介面只是 **提示定義**。它沒有硬編碼任何 `read_file`、`list_directory` 或其他 MCP 工具的知識。整個流程如下：
1. **伺服器啟動：** `StdioMcpTransport` 啟動 `@modelcontextprotocol/server-filesystem` npm 套件作為子程序  
2. **工具發現：** `McpClient` 向伺服器發送 `tools/list` JSON-RPC 請求，伺服器回傳工具名稱、描述和參數結構（例如 `read_file` — *「讀取檔案完整內容」* — `{ path: string }`）  
3. **結構注入：** `McpToolProvider` 將這些發現的結構包裝並提供給 LangChain4j 使用  
4. **LLM 決策：** 當呼叫 `FileAgent.readFile(path)` 時，LangChain4j 將系統訊息、使用者訊息、**以及工具結構清單** 發送給 LLM。LLM 閱讀工具描述並產生工具呼叫（例如 `read_file(path="/some/file.txt")`）  
5. **執行：** LangChain4j 攔截工具呼叫，透過 MCP 用戶端路由回 Node.js 子程序，獲取結果並反饋給 LLM  

這是上述相同的[工具發現](../../../05-mcp)機制，但特別用於代理工作流程中。`@SystemMessage` 和 `@UserMessage` 註解引導 LLM 的行為，而注入的 `ToolProvider` 提供其**能力**— LLM 於執行時串接兩者。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天嘗試：** 打開 [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) 並詢問：  
> - 「這個代理如何知道要呼叫哪個 MCP 工具？」  
> - 「如果我從代理建構器中移除 ToolProvider 會發生什麼？」  
> - 「工具結構如何傳遞到 LLM？」  

#### 回應策略

設定 `SupervisorAgent` 時，可指定當子代理完成任務後，如何形成對使用者的最終回答。下圖顯示三種可用策略 — LAST 直接回傳最後代理輸出，SUMMARY 透過 LLM 綜合所有輸出，SCORED 則依原始請求評分後選擇較高者：

<img src="../../../translated_images/zh-HK/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Supervisor 形成最終回應的三種策略 — 根據是否想要最後代理輸出、綜合摘要，或最高評分結果選擇。*

可用策略包括：

| 策略 | 描述 |
|----------|-------------|
| **LAST** | 監督者回傳最後一個子代理或工具的輸出。適合當工作流程最後的代理專門產生完整最終答案（例如研究流程中的「摘要代理」）。 |  
| **SUMMARY** | 監督者使用其內部語言模型（LLM）綜合整體互動和所有子代理產出，回傳該摘要作為最終回答。提供乾淨聚合的答案。 |  
| **SCORED** | 系統使用內部 LLM 針對 LAST 回應和 SUMMARY 摘要，對原始使用者請求評分，回傳得分較高的結果。 |  

完整實作請參考 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天嘗試：** 打開 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 並詢問：  
> - 「Supervisor 如何決定要呼叫哪些代理？」  
> - 「Supervisor 與 Sequential 工作流程模式有何不同？」  
> - 「如何自訂 Supervisor 的規劃行為？」  

#### 理解輸出

執行範例時，您會看到 Supervisor 如何協調多個代理的結構化演練。各部分含義如下：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**標題**介紹工作流程概念：從檔案讀取到報告生成的專注流程。

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
  
**工作流程圖**顯示代理間資料流。每位代理有明確角色：  
- **FileAgent** 使用 MCP 工具讀取檔案並將原始內容儲存至 `fileContent`  
- **ReportAgent** 使用該內容，產生結構化報告於 `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**使用者請求**顯示任務。Supervisor 解析後決定呼叫 FileAgent → ReportAgent。

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
  
**Supervisor 編排**展示兩步驟流程：  
1. **FileAgent** 透過 MCP 讀取檔案並儲存內容  
2. **ReportAgent** 接收內容並生成結構化報告  

Supervisor 根據使用者請求**自主**做出這些決策。

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

範例示範 agentic 模組多項進階功能。我們細看 Agentic 範圍和代理偵聽器。

**Agentic 範圍**示範代理用 `@Agent(outputKey="...")` 儲存結果的共享記憶體，允許：  
- 後續代理可存取先前代理輸出  
- Supervisor 綜合形成最終回應  
- 您可檢視各代理的產物  

下圖顯示 Agentic 範圍如何作為檔案到報告流程中的共享記憶體 —— FileAgent 在 `fileContent` 寫出，ReportAgent 讀取並寫入 `report`：

<img src="../../../translated_images/zh-HK/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic 範圍為共享記憶體 — FileAgent 寫入 `fileContent`，ReportAgent 讀取該內容並寫入 `report`，您的程式碼讀取最終結果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent 的原始檔案數據
String report = scope.readState("report");            // ReportAgent 的結構化報告
```
  
**代理偵聽器**支援監控及調試代理執行。範例中逐步輸出來自 AgentListener，鉤掛於每次代理呼叫：  
- **beforeAgentInvocation** - 當 Supervisor 選擇代理時呼叫，可查看被選代理及原因  
- **afterAgentInvocation** - 代理完成時呼叫，展示結果  
- **inheritedBySubagents** - 若為 true，監控層級內所有代理  

下圖呈現完整代理偵聽器生命周期，包括如何用 `onError` 處理代理執行期間的錯誤：

<img src="../../../translated_images/zh-HK/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*代理偵聽器鉤掛執行生命周期 — 監控代理開始、完成或錯誤狀態。*

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
  
除 Supervisor 模式外，`langchain4j-agentic` 模組還提供多種強大工作流程模式。下圖展示全部五種 — 從簡單順序流程到人機審核工作流程：

<img src="../../../translated_images/zh-HK/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*五種代理協調工作流程模式 — 從簡單順序到人機審核流程。*

| 模式 | 描述 | 使用場景 |
|---------|-------------|----------|
| **順序式** | 依序執行代理，輸出流向下一個 | 流程：研究 → 分析 → 報告 |
| **並行式** | 同時並行執行代理 | 獨立任務：天氣 + 新聞 + 股票 |
| **迴圈式** | 重複迭代直到條件達成 | 品質評分：持續調整直到分數≥0.8 |
| **條件式** | 根據條件路由 | 分類 → 導向專家代理 |
| **人機協同** | 加入人工審核點 | 審核流程，內容審查 |

## 關鍵概念

了解 MCP 和 agentic 模組運作後，來總結何時選用哪個方法。

MCP 最大優勢之一是其不斷擴展的生態系統。下圖展示一個通用協定如何連接您的 AI 應用和各種 MCP 伺服器 — 包括檔案系統、資料庫、GitHub、郵件、網頁爬蟲等：

<img src="../../../translated_images/zh-HK/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 創建通用協定生態系統 — 任何 MCP 相容伺服器均可與任何 MCP 相容客戶端搭配，實現跨應用的工具共享。*

**MCP** 適合想利用現有工具生態系統、建立多應用共享的工具、用標準協定整合第三方服務，或在不改變程式碼下切換工具實作的場景。

**Agentic 模組** 則適合想用宣告式代理定義（`@Agent` 註解）、需要工作流程編排（順序、迴圈、並行）、偏好介面化代理設計而非命令式代碼，或多代理共享輸出 (`outputKey`) 的應用。

**Supervisor 代理模式** 適合工作流程事先無法預測，需要 LLM 決策、多個專業代理動態編排、構建會話系統路由不同能力，或想要最具彈性和適應性的代理行為。

以下比較協助您在自訂 `@Tool` 方法（第 04 模組）和 MCP 工具（本模組）間做出選擇 — 自訂工具提供緊密耦合及完整型別安全，適用於應用特定邏輯；MCP 工具則提供標準化、可重複使用的整合：

<img src="../../../translated_images/zh-HK/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*何時使用自訂 @Tool 方法 vs MCP 工具 — 自訂工具適合有完整型別安全的應用邏輯，MCP 工具則適用跨應用通用的標準整合。*

## 恭喜！

您已完成 LangChain4j 初學者課程全部五個模組！回顧整個學習旅程 — 從基本聊天到 MCP 驅動的 agentic 系統：

<img src="../../../translated_images/zh-HK/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*您的完整學習旅程 — 從基礎聊天到 MCP 推動的 agentic 系統。*

您學到了：

- 如何構建具記憶功能的對話式 AI（模組 01）  
- 適用不同任務的提示工程模式（模組 02）  
- 基於文件地面真實回答（RAG）（模組 03）  
- 使用自訂工具建立基礎 AI 代理（助理）（模組 04）  
- 使用 LangChain4j MCP 與 Agentic 模組整合標準化工具（模組 05）  

### 接下來呢？

完成模組後，探索 [測試指南](../docs/TESTING.md)，了解 LangChain4j 的測試概念與實作。

**官方資源：**  
- [LangChain4j 文件](https://docs.langchain4j.dev/) — 完整使用手冊與 API 參考  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) — 原始碼與範例  
- [LangChain4j 教學](https://docs.langchain4j.dev/tutorials/) — 各種使用情境逐步教學  

感謝您完成此課程！

---

**導覽：** [← 上一節：模組 04 - 工具](../04-tools/README.md) | [回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件是使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威資料來源。對於關鍵資訊，建議使用專業人工翻譯。本公司不對因使用本翻譯而產生的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->