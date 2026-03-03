# Module 05: 模型上下文協議 (MCP)

## 目錄

- [你將學習什麼](../../../05-mcp)
- [甚麼是 MCP？](../../../05-mcp)
- [MCP 如何運作](../../../05-mcp)
- [代理模組](../../../05-mcp)
- [執行範例](../../../05-mcp)
  - [先決條件](../../../05-mcp)
- [快速開始](../../../05-mcp)
  - [檔案操作 (Stdio)](../../../05-mcp)
  - [監督代理](../../../05-mcp)
    - [執行示範](../../../05-mcp)
    - [監督者如何運作](../../../05-mcp)
    - [回應策略](../../../05-mcp)
    - [理解輸出](../../../05-mcp)
    - [代理模組功能說明](../../../05-mcp)
- [核心概念](../../../05-mcp)
- [恭喜完成！](../../../05-mcp)
  - [下一步？](../../../05-mcp)

## 你將學習什麼

你已經建立了對話式人工智能，精通提示詞，能將回應依據文件基礎，並且建立了帶有工具的代理。但這些工具都是為你的特定應用自訂建造的。如果你可以讓你的AI存取一個標準化的工具生態系統，且任何人都能建立並分享工具呢？在本模組中，你將學習透過模型上下文協議（Model Context Protocol，MCP）和 LangChain4j 的代理模組來做到這一點。我們先展示一個簡單的 MCP 檔案閱讀器，然後展示如何輕鬆整合到使用監督代理模式的進階代理工作流程中。

## 甚麼是 MCP？

模型上下文協議（MCP）正是提供這樣一個標準方法，讓人工智能應用能發現並使用外部工具。你不需要為每個資料來源或服務編寫自訂整合，而是連接到以一致格式公開其能力的 MCP 伺服器。你的 AI 代理就能自動發現並使用這些工具。

下圖展示了差異 — 沒有 MCP 時，每個整合都需要自訂一對一連接；有了 MCP，一個協議就能將你的應用連至任何工具：

<img src="../../../translated_images/zh-MO/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 之前：複雜的一對一整合。MCP 之後：一個協議，無限可能。*

MCP 解決了 AI 開發中一個根本性問題：每個整合都是自訂的。想存取 GitHub？自訂程式碼。想讀取檔案？自訂程式碼。想查詢資料庫？自訂程式碼。而且這些整合無法與其他 AI 應用共用。

MCP 將此標準化。MCP 伺服器用清晰的描述與架構公開工具，任何 MCP 用戶端都能連接、發現可用工具並使用。建一次，處處用。

下圖說明了這架構 — 單個 MCP 用戶端（你的 AI 應用）連接多個 MCP 伺服器，每個伺服器透過標準協議公開自己的一組工具：

<img src="../../../translated_images/zh-MO/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型上下文協議架構—標準化工具發現與執行*

## MCP 如何運作

在底層，MCP 採用分層架構。你的 Java 應用（MCP 用戶端）發現可用工具，透過傳輸層（Stdio 或 HTTP）送出 JSON-RPC 請求，MCP 伺服器執行操作並回傳結果。下圖細分此協議的每層：

<img src="../../../translated_images/zh-MO/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 底層運作方式 — 用戶端發現工具，交換 JSON-RPC 訊息，透過傳輸層執行操作。*

**伺服器 - 用戶端架構**

MCP 採用用戶端伺服器模型。伺服器提供工具——讀檔案、查詢資料庫、調用 API。用戶端（你的 AI 應用）連接伺服器並使用其工具。

要在 LangChain4j 使用 MCP，加入此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**工具發現**

當你的用戶端連接至 MCP 伺服器，它會詢問「你有哪些工具？」伺服器回應一份可用工具列表，每個工具都帶有描述和參數架構。你的 AI 代理可以根據用戶請求決定使用哪些工具。下圖展示此握手過程 — 用戶端送出 `tools/list` 請求，而伺服器回傳其帶說明與參數架構的工具：

<img src="../../../translated_images/zh-MO/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在啟動時發現可用工具——現在它知道有哪些功能可用，並能決定要用哪些。*

**傳輸機制**

MCP 支援不同傳輸機制。兩種選擇是 Stdio（用於本地子程序通訊）和可串流 HTTP（用於遠端伺服器）。本模組示範 Stdio 傳輸：

<img src="../../../translated_images/zh-MO/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 傳輸機制：HTTP 用於遠端伺服器，Stdio 用於本地程序*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

用於本地程序。你的應用啟動伺服器作為子程序，透過標準輸入/輸出通訊。適用於檔案系統存取或命令列工具。

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

`@modelcontextprotocol/server-filesystem` 伺服器公開以下工具，皆限制於你指定的目錄：

| 工具 | 描述 |
|------|-------------|
| `read_file` | 讀取單一檔案內容 |
| `read_multiple_files` | 一次讀取多個檔案 |
| `write_file` | 建立或覆寫檔案 |
| `edit_file` | 有針對性的尋找並取代編輯 |
| `list_directory` | 列出路徑下的檔案和目錄 |
| `search_files` | 遞迴搜尋符合模式的檔案 |
| `get_file_info` | 取得檔案元資料（大小、時間戳、權限） |
| `create_directory` | 建立目錄（包含父目錄） |
| `move_file` | 移動或重新命名檔案或目錄 |

下圖展示 Stdio 傳輸的運作流程 — 你的 Java 應用啟動 MCP 伺服器為子程序，透過 stdin/stdout 管線通訊，無涉及網路或 HTTP：

<img src="../../../translated_images/zh-MO/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 傳輸運作 — 你的應用啟動 MCP 伺服器為子程序並透過 stdin/stdout 管線通訊。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 開啟 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) 並詢問：
> - "Stdio 傳輸如何運作？什麼時候該用它而非 HTTP？"
> - "LangChain4j 如何管理啟動的 MCP 伺服器程序生命週期？"
> - "讓 AI 存取檔案系統有何安全風險？"

## 代理模組

雖然 MCP 提供標準化工具，LangChain4j 的 **代理模組** 則提供宣告式方式建構代理，能協調這些工具。`@Agent` 註釋與 `AgenticServices` 讓你用介面而非命令式程式碼定義代理行為。

在本模組中，你將探索 **監督代理** 模式——一種進階的代理式 AI 方法，其中「監督者」代理根據用戶請求動態決定調用哪些子代理。我們會結合兩概念，讓其中一個子代理具備 MCP 支援的檔案存取功能。

要使用代理模組，加入此 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **注意：** `langchain4j-agentic` 模組使用獨立的版本屬性（`langchain4j.mcp.version`），因為它發佈時間表與核心 LangChain4j 函式庫不同。

> **⚠️ 實驗性質：** `langchain4j-agentic` 模組屬於 **實驗性**，後續可能會變動。穩定建構 AI 助手的方式仍是使用 `langchain4j-core` 和自訂工具（第04模組）。

## 執行範例

### 先決條件

- 完成 [第04模組 - 工具](../04-tools/README.md)（本模組建立於自訂工具概念並與 MCP 工具比較）
- 根目錄有 `.env` 檔，內含 Azure 憑證（由 Module 01 中 `azd up` 建立）
- Java 21+、Maven 3.9+
- Node.js 16+ 和 npm（用於 MCP 伺服器）

> **注意：** 若尚未設定環境變數，請參閱 [Module 01 - 介紹](../01-introduction/README.md) 中部署指引（`azd up` 會自動建立 `.env` 檔），或將 `.env.example` 複製為根目錄的 `.env` 並填入你的數值。

## 快速開始

**使用 VS Code：** 在檔案總管中右鍵任一示範檔，選擇 **「Run Java」**，或使用執行與除錯面板的啟動設定（務必先配置好 `.env` 的 Azure 憑證）。

**使用 Maven：** 你也可以從命令列執行以下範例。

### 檔案操作 (Stdio)

示範基於本地子程序的工具。

**✅ 不需先決條件** — MCP 伺服器會自動啟動。

**使用啟動腳本（推薦）：**

啟動腳本會自動從根目錄 `.env` 檔載入環境變數：

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

**使用 VS Code：** 右鍵 `StdioTransportDemo.java` 並選擇 **「Run Java」**（確保 `.env` 已配置）。

應用程式會自動啟動檔案系統 MCP 伺服器並讀取當地檔案。留意子程序管理如何為你處理。

**預期輸出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### 監督代理

**監督代理模式**是一種**彈性**的代理式 AI。監督者使用大型語言模型 (LLM) 自主判斷根據用戶的請求要調用哪些代理。在下一個範例中，我們結合 MCP 支援的檔案存取與 LLM 代理，創建一個監督的檔案讀取 → 報告工作流程。

示範中，`FileAgent` 使用 MCP 檔案系統工具讀取檔案，而 `ReportAgent` 生成結構化報告，包含一段執行摘要（一句話）、三個重點和建議。監督者自主協調此流程：

<img src="../../../translated_images/zh-MO/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*監督者使用其 LLM 判斷要調用哪些代理及順序──不需硬編碼路由。*

檔案到報告的具體工作流程如下：

<img src="../../../translated_images/zh-MO/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 透過 MCP 工具讀取檔案，然後 ReportAgent 將原始內容轉換成結構化報告。*

每個代理將輸出儲存在 **代理範圍（Agentic Scope）** 中（共享記憶體），讓下游代理可以存取先前結果。這顯示 MCP 工具如何無縫整合至代理式工作流程──監督者不需知道檔案如何被讀取，只需知道 `FileAgent` 能做到。

#### 執行示範

啟動腳本會自動從根目錄 `.env` 檔載入環境變數：

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

**使用 VS Code：** 右鍵 `SupervisorAgentDemo.java` 並選擇 **「Run Java」**（確保 `.env` 已配置）。

#### 監督者如何運作

在建構代理前，你需要將 MCP 傳輸連接到用戶端並封裝為 `ToolProvider`。這是 MCP 伺服器工具能供代理使用的方式：

```java
// 從傳輸創建一個MCP客戶端
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// 把客戶端包裝成ToolProvider — 這將MCP工具連接到LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

現在你可以將 `mcpToolProvider` 注入任何需要 MCP 工具的代理：

```java
// 第一步：FileAgent 使用 MCP 工具讀取文件
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 具備 MCP 文件操作工具
        .build();

// 第二步：ReportAgent 產生結構化報告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// 主管負責協調文件到報告的工作流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 返回最終報告
        .build();

// 主管根據請求決定調用哪些代理人
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### 回應策略

設定 `SupervisorAgent` 時，你要指定該代理在子代理完成任務後，如何向用戶形成最終回應。下圖展示了三種可用策略——LAST 直接返回最後一個代理的輸出，SUMMARY 透過 LLM 將所有輸出合成摘要，SCORED 則選擇針對原始請求得分較高的輸出：

<img src="../../../translated_images/zh-MO/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*三種監督者形成最終回應的策略——根據你想要最後代理輸出、合成摘要，或最佳分數選項做選擇。*

可用策略如下：

| 策略 | 描述 |
|----------|-------------|
| **LAST** | 監督者返回最後呼叫的子代理或工具的輸出。當工作流程中最後的代理特別設計為產生完整終結答案時（例如研究管線中的「摘要代理」），此策略有用。 |
| **SUMMARY** | 監督者使用其內部大型語言模型 (LLM) 合成整個互動與所有子代理輸出的摘要，並將該摘要作為最終回應。這為用戶提供乾淨且彙整的答案。 |
| **SCORED** | 系統使用內部 LLM 針對最後回應和整體摘要，依原始用戶請求進行評分，並返回得分較高的輸出。 |
完整實作請參考 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)。

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 並詢問：
> - “Supervisor 如何決定要調用哪些代理？”
> - “Supervisor 和 Sequential 工作流程模式有何差別？”
> - “我如何定制 Supervisor 的規劃行為？”

#### 理解輸出內容

運行此示範時，您會看到如何 Supervisor 協調多個代理的結構化演示。以下說明每個部分的意義：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**標題** 介紹工作流程概念：從檔案讀取到報告生成的聚焦管道。

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

**工作流程圖** 展示代理間的資料流。每個代理有特定角色：
- **FileAgent** 使用 MCP 工具讀取檔案並將原始內容存入 `fileContent`
- **ReportAgent** 消費該內容並產生結構化報告放入 `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**使用者請求** 展示任務內容。Supervisor 解析後決定依序調用 FileAgent → ReportAgent。

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

**Supervisor 協調** 展示實際執行的兩步流程：
1. **FileAgent** 透過 MCP 讀取檔案並儲存內容
2. **ReportAgent** 接收內容並產生結構化報告

Supervisor 根據使用者請求 **自主地** 作出這些決策。

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

#### Agentic 模組功能解說

此範例展示了 agentic 模組的幾項進階功能。讓我們仔細看看 Agentic Scope 與 Agent Listeners。

**Agentic Scope** 展示代理們使用 `@Agent(outputKey="...")` 存放結果的共用記憶體。它允許：
- 後續代理訪問前面代理的輸出
- Supervisor 合成最終回應
- 您檢查各代理產出的內容

下圖示意 Agentic Scope 作為檔案到報告工作流程中共享記憶體的運作—FileAgent 將輸出寫入 `fileContent`，ReportAgent 讀取該資料並寫入自己的 `report`：

<img src="../../../translated_images/zh-MO/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope 作為共享記憶體運作—FileAgent 寫入 `fileContent`，ReportAgent 讀取並寫入 `report`，您的程式碼讀取最終結果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // 來自FileAgent的原始文件數據
String report = scope.readState("report");            // 來自ReportAgent的結構化報告
```

**Agent Listeners** 讓您監控和除錯代理執行。示範中您看到的逐步輸出來自掛載於每次代理調用的 AgentListener：
- **beforeAgentInvocation** - Supervisor 選擇代理時呼叫，讓您瞭解被選中哪個代理和原因
- **afterAgentInvocation** - 代理完成時呼叫，展示結果
- **inheritedBySubagents** - 若為真，監控層級結構中所有代理

下圖展示完整代理監聽器生命週期，包括 `onError` 如何處理代理執行時錯誤：

<img src="../../../translated_images/zh-MO/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners 掛載於執行生命週期—監控代理開始、完成或遇到錯誤的時機。*

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

除了 Supervisor 模式外，`langchain4j-agentic` 模組還提供多種強大的工作流程模式。下圖展示全部五種—從簡單的順序流水線到有人類介入的核准工作流程：

<img src="../../../translated_images/zh-MO/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*五種代理工作流程模式—從簡單順序流水線到有人類介入的核准流程。*

| 模式 | 說明 | 使用案例 |
|---------|-------------|----------|
| **Sequential** | 依序執行代理，輸出流向下一個 | 流水線流程：研究 → 分析 → 報告 |
| **Parallel** | 同時執行多個代理 | 獨立任務：天氣 + 新聞 + 股票 |
| **Loop** | 持續迭代直到條件達成 | 品質打分：精煉直到分數 ≥ 0.8 |
| **Conditional** | 根據條件分流 | 分類 → 導向專家代理 |
| **Human-in-the-Loop** | 加入人工檢查點 | 核准流程、內容審查 |

## 主要概念

現在您已實際體驗 MCP 和 agentic 模組，讓我們總結各種方法的適用時機。

MCP 最大優勢之一是其蓬勃發展的生態系統。下圖顯示單一通用協定如何串連您的 AI 應用與各式 MCP 伺服器—涵蓋檔案系統、資料庫、GitHub、電郵、網頁爬蟲等：

<img src="../../../translated_images/zh-MO/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 建立通用協定生態系統—任一 MCP 相容伺服器可搭配任一 MCP 相容客戶端，實現跨應用共享工具。*

**MCP** 適合想利用現有工具生態系、打造可被多應用共用的工具、以標準協定整合第三方服務，或希望不改程式碼即可切換工具實作的情況。

**Agentic 模組** 適用於希望用 `@Agent` 註解定義宣告式代理、需要流程協調（順序、迴圈、並行）、偏好介面導向代理設計而非命令式代碼，或多個代理需透過 `outputKey` 共享輸出的場景。

**Supervisor Agent 模式** 在工作流程前期難以預測並希望由 LLM 決策、有多個專業代理需動態協調、打造會話系統能導向不同能力，或想要最大彈性與自適應代理行為時特別適用。

為幫助您在本單元中的客製 `@Tool` 方法與 MCP 工具間做選擇，以下比較強調關鍵差異 — 客製工具提供緊密耦合與完整類型安全體驗於應用邏輯，MCP 工具則提供標準化、可重複使用的整合方案：

<img src="../../../translated_images/zh-MO/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*使用自訂 @Tool 方法還是 MCP 工具—自訂工具適合應用專屬邏輯且具全類型安全，MCP 工具適合跨應用協作的標準化整合。*

## 恭喜！

您已完成 LangChain4j 初學者課程五大模組！以下是完整學習歷程回顧—從基礎聊天到 MCP 驅動的 agentic 系統：

<img src="../../../translated_images/zh-MO/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*您橫跨五大模組的學習旅程—從基礎聊天到 MCP 驅動的 agentic 系統。*

您已學會：

- 如何建構具記憶功能的會話型 AI（模組 01）
- 不同任務的提示工程範式（模組 02）
- 用 RAG 使回應扎根於您的文件（模組 03）
- 用客製工具打造基本 AI 代理（助理）（模組 04）
- 與 LangChain4j MCP 和 Agentic 模組整合標準工具（模組 05）

### 接下來？

完成模組後，請參考 [測試指南](../docs/TESTING.md) 瞭解 LangChain4j 測試概念實作。

**官方資源：**
- [LangChain4j 文件](https://docs.langchain4j.dev/) — 完整指南與 API 參考
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) — 原始碼與範例
- [LangChain4j 教學](https://docs.langchain4j.dev/tutorials/) — 針對多種使用案例的逐步教學

感謝您完成本課程！

---

**導覽：** [← 上一節：模組 04 - 工具](../04-tools/README.md) | [返回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件乃使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件以其原生語言版本為權威資料來源。如涉及重要資訊，建議聘請專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->