# Module 05: 模型上下文協議 (MCP)

## 目錄

- [你將學習什麼](../../../05-mcp)
- [什麼是 MCP？](../../../05-mcp)
- [MCP 如何運作](../../../05-mcp)
- [Agentic 模組](../../../05-mcp)
- [執行範例](../../../05-mcp)
  - [先決條件](../../../05-mcp)
- [快速開始](../../../05-mcp)
  - [檔案操作 (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [執行示範](../../../05-mcp)
    - [Supervisor 如何運作](../../../05-mcp)
    - [回應策略](../../../05-mcp)
    - [理解輸出結果](../../../05-mcp)
    - [Agentic 模組功能說明](../../../05-mcp)
- [關鍵概念](../../../05-mcp)
- [恭喜！](../../../05-mcp)
  - [接下來？](../../../05-mcp)

## 你將學習什麼

你已經建立了會話式 AI，精通提示詞，將回應依據文件落地，並創建了帶有工具的代理。但這些工具都是為你的特定應用量身打造的。如果你能讓你的 AI 訪問一套標準化的工具生態系統，任何人都能創建並共享這些工具，那會怎樣呢？在本模組中，你將學習如何使用模型上下文協議（Model Context Protocol，MCP）和 LangChain4j 的 agentic 模組來實現這一點。我們首先展示一個簡單的 MCP 檔案讀取工具，然後展示它如何輕鬆整合進基於 Supervisor Agent 模式的高級 agentic 工作流程。

## 什麼是 MCP？

模型上下文協議（MCP）正是提供了這樣一種標準方式，讓 AI 應用發現並使用外部工具。你不必為每個資料來源或服務編寫自訂整合，而是連接到 MCP 伺服器，這些伺服器會以一致格式公開其功能。你的 AI 代理隨後可以自動發現並使用這些工具。

下圖展示了區別 —— 沒有 MCP，每個整合都需要自訂點對點連接；使用 MCP，單一協議即可連接你的應用到任何工具：

<img src="../../../translated_images/zh-HK/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 出現前：複雜的點對點整合。MCP 出現後：一個協議，無限可能。*

MCP 解決了 AI 開發的一個根本問題：每次整合都是自訂的。想使用 GitHub？自訂程式碼。想讀取檔案？自訂程式碼。想查詢資料庫？自訂程式碼。且這些整合互不相容。

MCP 則使這一切標準化。MCP 伺服器會以清晰描述和架構公開工具。任何 MCP 客戶端都能連線、發現可用工具並使用。只需構建一次，到處使用。

下圖說明了此架構——一個 MCP 客戶端（你的 AI 應用）連接多個 MCP 伺服器，各自透過標準協議公開它們的工具集：

<img src="../../../translated_images/zh-HK/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型上下文協議架構 —— 標準化工具的發現與執行*

## MCP 如何運作

在底層，MCP 採用分層架構。你的 Java 應用（MCP 客戶端）發現可用工具，透過傳輸層（Stdio 或 HTTP）發送 JSON-RPC 請求，MCP 伺服器執行操作並返回結果。下圖分解此協議的各層：

<img src="../../../translated_images/zh-HK/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 底層運作方式 —— 客戶端發現工具，交換 JSON-RPC 訊息，並透過傳輸層執行操作。*

**伺服器-客戶端架構**

MCP 採用客戶端伺服器模型。伺服器提供工具 —— 讀取檔案、查詢資料庫、呼叫 API。客戶端（你的 AI 應用）連接到伺服器並使用其工具。

要在 LangChain4j 中使用 MCP，請新增以下 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**工具發現**

當你的客戶端連接到 MCP 伺服器時，它會詢問「你有什麼工具？」伺服器回應可用工具清單，每個工具有描述和參數結構。你的 AI 代理可以根據用戶需求決定使用哪些工具。下圖顯示這個握手過程 —— 客戶端發送 `tools/list` 請求，伺服器返回其可用工具與描述和參數結構：

<img src="../../../translated_images/zh-HK/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在啟動時發現可用工具——它現在知道有哪些功能，並可以決定使用哪些。*

**傳輸機制**

MCP 支援不同的傳輸機制。有兩種選項：Stdio（用於本地子程序通訊）和可串流的 HTTP（用於遠端伺服器）。本模組示範 Stdio 傳輸：

<img src="../../../translated_images/zh-HK/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 傳輸機制：遠端伺服器使用 HTTP，本地程序使用 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

用於本地程序。你的應用啟動一個伺服器作為子程序，並透過標準輸入/輸出通訊。適合檔案系統存取或命令列工具。

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

`@modelcontextprotocol/server-filesystem` 伺服器公開以下工具，均沙盒限制於你指定的目錄：

| 工具 | 描述 |
|------|------|
| `read_file` | 讀取單一檔案的內容 |
| `read_multiple_files` | 一次讀取多個檔案 |
| `write_file` | 創建或覆蓋檔案 |
| `edit_file` | 針對性地執行尋找並替換的編輯 |
| `list_directory` | 列出目錄路徑下的檔案及資料夾 |
| `search_files` | 遞迴搜索符合條件的檔案 |
| `get_file_info` | 取得檔案元資料（大小、時間戳、權限） |
| `create_directory` | 創建資料夾（包含父資料夾） |
| `move_file` | 移動或重新命名檔案或資料夾 |

下圖展示 Stdio 傳輸在運行時的工作流程 —— 你的 Java 應用將 MCP 伺服器當作子程序啟動，兩者透過 stdin/stdout 管道通訊，無需使用網路或 HTTP：

<img src="../../../translated_images/zh-HK/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 傳輸示意 —— 你的應用啟動 MCP 伺服器作為子程序，透過 stdin/stdout 管道通訊。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天:** 開啟 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) 並提問：
> - 「Stdio 傳輸如何運作？何時該使用它而非 HTTP？」
> - 「LangChain4j 如何管理被啟動的 MCP 伺服器程式生命週期？」
> - 「讓 AI 訪問檔案系統有哪些安全風險？」

## Agentic 模組

雖然 MCP 提供了標準化的工具，LangChain4j 的 **agentic 模組** 則提供了一種宣告式的方式，構建可協調使用這些工具的代理。`@Agent` 註解和 `AgenticServices` 讓你通過介面定義代理行為，而非命令式程式碼。

在本模組中，你將探索 **Supervisor Agent** 模式 —— 一種進階的 agentic AI 方案，「監督者」代理會根據用戶請求動態決定調用哪些子代理。我們將結合這兩個概念，讓其中一個子代理具備 MCP 支援的檔案存取能力。

要使用 agentic 模組，請新增以下 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **注意：** `langchain4j-agentic` 模組有單獨的版本屬性 (`langchain4j.mcp.version`)，因為它的發布節奏與核心 LangChain4j 庫不同。

> **⚠️ 實驗性:** `langchain4j-agentic` 模組目前是 **實驗性**，未來可能變動。穩定打造 AI 助手的方式仍是 `langchain4j-core` 配合自訂工具 (模組 04)。

## 執行範例

### 先決條件

- 完成 [Module 04 - Tools](../04-tools/README.md)（本模組基於自訂工具概念，並比較 MCP 工具）
- 專案根目錄有含 Azure 認證的 `.env` 檔（由 Module 01 中的 `azd up` 建立）
- Java 21 以上，Maven 3.9 以上
- Node.js 16+ 及 npm（用於 MCP 伺服器）

> **備註：** 如果尚未配置環境變數，請參閱[Module 01 - Introduction](../01-introduction/README.md)的部署指示（`azd up` 會自動創建 `.env`），或將專案根目錄中的 `.env.example` 複製為 `.env` 並填入資料。

## 快速開始

**使用 VS Code：** 只需在檔案總管中右鍵點擊任何示範檔案，選擇 **「執行 Java」**，或使用「執行與除錯」面板中的啟動配置（確保先配置好 `.env` 及 Azure 認證）。

**使用 Maven：** 也可以從命令行執行以下範例。

### 檔案操作 (Stdio)

展示本地子程序基礎的工具。

**✅ 無需任何先決條件** — MCP 伺服器會自動啟動。

**使用啟動腳本（推薦）：**

啟動腳本會自動從根目錄 `.env` 檔載入環境變數：

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

**使用 VS Code：** 右鍵點擊 `StdioTransportDemo.java` 並選擇 **「執行 Java」**（確保 `.env` 配置正確）。

應用會自動啟動一個檔案系統 MCP 伺服器並讀取本地檔案。注意子程序管理是自動處理的。

**預期輸出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agent 模式** 是一種 **靈活的** agentic AI 形式。監督者透過大型語言模型 (LLM) 自主決定根據用戶請求調用哪些代理。在接下來的範例中，我們將結合 MCP 支援的檔案存取與 LLM 代理，打造一個經過監督的檔案讀取 → 報告生成流程。

示範中，`FileAgent` 使用 MCP 檔案系統工具讀取檔案，`ReportAgent` 則生成一份結構化報告，其中包含執行摘要（1 句）、3 個重點與建議。Supervisor 自動協調此流程：

<img src="../../../translated_images/zh-HK/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*監督者利用其 LLM 決定調用哪些代理及順序 —— 無需硬編碼路由。*

我們文件到報告的具體工作流程長這樣：

<img src="../../../translated_images/zh-HK/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 通過 MCP 工具讀取檔案，再由 ReportAgent 將原始內容轉換成結構化報告。*

每個代理將輸出儲存在 **Agentic Scope**（共用記憶體）中，讓後續代理可以存取之前結果。這展示 MCP 工具如何無縫整合進 agentic 工作流程 —— 監督者無需知道檔案如何讀取，只知道 `FileAgent` 能做這件事。

#### 執行示範

啟動腳本會自動從根目錄 `.env` 檔載入環境變數：

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

**使用 VS Code：** 右鍵點擊 `SupervisorAgentDemo.java` 並選擇 **「執行 Java」**（確保 `.env` 配置正確）。

#### Supervisor 如何運作

在構建代理前，你需要將 MCP 傳輸接入客戶端，並包裝為 `ToolProvider`。這樣 MCP 伺服器的工具才會對代理可用：

```java
// 從傳輸層建立 MCP 用戶端
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// 將用戶端包裝為 ToolProvider — 這將 MCP 工具橋接到 LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

現在你可以將 `mcpToolProvider` 注入任何需要 MCP 工具的代理：

```java
// 第一步：FileAgent 使用 MCP 工具讀取檔案
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 擁有用於檔案操作的 MCP 工具
        .build();

// 第二步：ReportAgent 生成結構化報告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// 主管負責協調檔案→報告的工作流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 返回最終報告
        .build();

// 主管根據請求決定調用哪些代理服務
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### 回應策略

配置 `SupervisorAgent` 時，需指定在子代理完成任務後，監督者如何向用戶形成最終回答。下圖顯示三種可用策略 —— LAST 直接回傳最後代理的輸出，SUMMARY 使用 LLM 綜合所有輸出生成摘要，SCORED 則選擇對原始請求評分較高的輸出：

<img src="../../../translated_images/zh-HK/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*監督者形成最終回應的三種策略 —— 根據你想要最後代理輸出、綜合摘要或最佳評分結果選擇。*

可用策略為：

| 策略 | 說明 |
|----------|-------------|
| **LAST** | 監督者回傳最後調用的子代理或工具輸出。當流程中最後一個代理特別設計為產生完整最終答案時非常實用（例如研究流程中的「摘要代理」）。 |
| **SUMMARY** | 監督者使用內部大型語言模型 (LLM) 綜合整個互動及所有子代理輸出，生成摘要並作為最終回應。這提供給用戶一份清晰的彙整答案。 |
| **SCORED** | 系統使用內部 LLM 給最後回應和綜合摘要分數，根據與原始用戶請求的匹配度評選，回傳得分較高者。 |
請參閱 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 的完整實作。

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天:** 開啟 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 並詢問：
> - 「Supervisor 如何決定調用哪些代理？」
> - 「Supervisor 與 Sequential 工作流程模式有何不同？」
> - 「我如何自訂 Supervisor 的規劃行為？」

#### 理解輸出結果

當你執行示範時，你會看到 Supervisor 如何協調多個代理的結構化流程。以下是每個區段的說明：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**標頭**介紹工作流程概念：從檔案讀取到報告產生的專注流程。

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

**工作流程圖**展示代理之間的數據流。每個代理有其特定角色：
- **FileAgent** 使用 MCP 工具讀取檔案並將原始內容儲存於 `fileContent`
- **ReportAgent** 消耗該內容並產生結構化報告於 `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**使用者請求**展示任務內容。Supervisor 解析後決定呼叫 FileAgent → ReportAgent。

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

**Supervisor 協調**展示兩步驟流程實作：
1. **FileAgent** 透過 MCP 讀取檔案並儲存內容
2. **ReportAgent** 收到內容並產生結構化報告

Supervisor 根據使用者請求 **自主** 做出這些決定。

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

本範例示範 agentic 模組中的多項進階功能。讓我們更仔細地看看 Agentic Scope 和 Agent Listeners。

**Agentic Scope** 顯示代理使用 `@Agent(outputKey="...")` 儲存結果時共享的記憶體。這允許：
- 後續代理存取先前代理的輸出
- Supervisor 綜合最終回應
- 你檢查每個代理產出的內容

下圖展示 Agentic Scope 作為檔案至報告的共享記憶體，FileAgent 將結果寫於 `fileContent` 鍵下，ReportAgent 讀取該內容並寫入自己的結果於 `report`：

<img src="../../../translated_images/zh-HK/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope 作為共享記憶體－FileAgent 寫入 `fileContent`，ReportAgent 讀取並寫入 `report`，你的程式碼則讀取最終結果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // 來自 FileAgent 的原始檔案數據
String report = scope.readState("report");            // 來自 ReportAgent 的結構化報告
```

**Agent Listeners** 讓你能監控及除錯代理的執行。示範中逐步輸出來自於一個掛勾於每個代理調用的 AgentListener：
- **beforeAgentInvocation** - 當 Supervisor 選擇代理時呼叫，讓你看到被挑選的代理與原因
- **afterAgentInvocation** - 代理完成後呼叫，顯示其結果
- **inheritedBySubagents** - 若為 true，監控整個代理階層中的所有代理

下圖展示完整的 Agent Listener 生命週期，包括 `onError` 如何處理代理執行時的錯誤：

<img src="../../../translated_images/zh-HK/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners 掛勾於執行生命週期－監控代理啟動、完成或遇錯狀態。*

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

除了 Supervisor 模式外，`langchain4j-agentic` 模組提供多種強大的工作流程模式。以下圖展示五種，從簡單的順序管線到帶有人類審核的流程：

<img src="../../../translated_images/zh-HK/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*五種代理工作流程模式－從簡單順序管線至帶有人類審核的流程。*

| 模式 | 描述 | 使用情境 |
|---------|-------------|----------|
| **Sequential** | 按順序執行代理，輸出流向下一個 | 管線：研究 → 分析 → 報告 |
| **Parallel** | 同時執行多個代理 | 獨立任務：天氣＋新聞＋股票 |
| **Loop** | 迭代直到條件達成 | 品質評分：精煉直到分數 ≥ 0.8 |
| **Conditional** | 根據條件路由 | 分類 → 指派專家代理 |
| **Human-in-the-Loop** | 加入人工檢核點 | 審核流程，內容檢查 |

## 重要概念

瞭解 MCP 與 agentic 模組的運作後，讓我們總結何時適合使用哪種方法。

MCP 最大優勢之一是其不斷壯大的生態系統。以下圖顯示單一通用協定如何連接你的 AI 應用與多種 MCP 伺服器——從檔案系統與資料庫存取到 GitHub、電子郵件、網頁爬蟲等服務：

<img src="../../../translated_images/zh-HK/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 創造通用協定生態系統——任何符合 MCP 的伺服器皆可與符合 MCP 的客戶端合作，實現跨應用共享工具。*

**MCP** 適用於你想利用現有工具生態系統，打造多應用共享的工具，整合第三方服務的標準協定，或想更換工具實作而不用變動程式碼。

**Agentic 模組** 適合需要用 `@Agent` 註解做宣告式代理定義，需要工作流程協調（順序、迴圈、並行），偏好介面導向代理設計而非命令式程式碼，或需要多代理共享輸出（`outputKey`）的場合。

**Supervisor Agent 模式** 在工作流程無法預先預測並需 LLM 決策時最合適，當你擁有多個專門代理需要動態協調，建立能路由至多種能力的對話系統，或者你想要最靈活且具有適應性的代理行為。

若欲比較模組 04 的自訂 `@Tool` 方法與本模組的 MCP 工具，以下圖著重關鍵取捨——自訂工具提供強耦合與完整型別安全以符合應用特定邏輯，MCP 工具則提供標準化、可重用整合：

<img src="../../../translated_images/zh-HK/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*何時使用自訂 @Tool 方法 vs MCP 工具－自訂工具適用特定應用邏輯與完整型別安全，MCP 工具提供跨應用的標準化整合。*

## 恭喜！

你已完成 LangChain4j 初學者課程的五個模組！以下是你一路走來的完整學習旅程——從基本聊天一直到 MCP 驅動的 Agentic 系統：

<img src="../../../translated_images/zh-HK/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*你經歷的所有五個模組學習旅程－從基本聊天到 MCP 驅動的 Agentic 系統。*

你學會了：

- 如何建構帶有記憶的對話式 AI（模組 01）
- 針對不同任務的提示工程模式（模組 02）
- 以 RAG 方式根據你的文件深化回應（模組 03）
- 使用自訂工具建立基本 AI 代理（助理）（模組 04）
- 利用 LangChain4j MCP 及 Agentic 模組整合標準化工具（模組 05）

### 接下來呢？

完成模組後，探索 [測試指南](../docs/TESTING.md) 以了解 LangChain4j 測試概念的實際應用。

**官方資源：**
- [LangChain4j 文件](https://docs.langchain4j.dev/) - 完整指南與 API 參考
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - 原始碼與範例
- [LangChain4j 教學](https://docs.langchain4j.dev/tutorials/) - 各種用例的逐步教學

感謝你完成本課程！

---

**導覽：** [← 上一頁：模組 04 - 工具](../04-tools/README.md) | [回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件是使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力追求準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的原文版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用此翻譯而引起的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->