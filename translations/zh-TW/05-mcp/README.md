# Module 05: 模型上下文協議 (MCP)

## 目錄

- [影片導覽](../../../05-mcp)
- [你將學到什麼](../../../05-mcp)
- [什麼是 MCP？](../../../05-mcp)
- [MCP 如何運作](../../../05-mcp)
- [代理模組](../../../05-mcp)
- [執行範例](../../../05-mcp)
  - [先決條件](../../../05-mcp)
- [快速開始](../../../05-mcp)
  - [檔案操作（Stdio）](../../../05-mcp)
  - [督導代理](../../../05-mcp)
    - [執行示範](../../../05-mcp)
    - [督導如何運作](../../../05-mcp)
    - [FileAgent 如何在執行時發現 MCP 工具](../../../05-mcp)
    - [回應策略](../../../05-mcp)
    - [理解輸出](../../../05-mcp)
    - [代理模組特性的說明](../../../05-mcp)
- [關鍵概念](../../../05-mcp)
- [恭喜你！](../../../05-mcp)
  - [接下來？](../../../05-mcp)

## 影片導覽

觀看本次直播說明如何開始使用本模組：

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## 你將學到什麼

你已經建立了會話式 AI，掌握了提示技巧、將回應基於文件做根據，並創建了帶有工具的代理。但所有那些工具都是針對特定應用客製化建造的。如果你能讓 AI 存取一個標準化的工具生態系，任何人都可以創建並分享呢？在本模組中，你將學習如何使用模型上下文協議（MCP）和 LangChain4j 的代理模組達成這個目標。我們首先展示一個簡單的 MCP 檔案讀取器，然後演示如何輕鬆整合進基於督導代理模式的進階代理工作流程。

## 什麼是 MCP？

模型上下文協議（MCP）正是為此而生——一種標準化方式，讓 AI 應用能夠發現並使用外部工具。不必為每個資料來源或服務撰寫自訂整合，你只需連接 MCP 伺服器，它們以一致格式暴露功能。你的 AI 代理隨後可以自動發現並使用這些工具。

下圖顯示兩者差異——沒有 MCP，每個整合都需要專屬的點對點連線；有了 MCP，一個協議能連接你的應用到任何工具：

<img src="../../../translated_images/zh-TW/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 之前：複雜的點對點整合。MCP 之後：一個協議，無限可能。*

MCP 解決了 AI 開發中的一個根本問題：每個整合都是專屬的。想要存取 GitHub？專屬程式碼。讀取檔案？專屬程式碼。查詢資料庫？專屬程式碼。而這些整合都無法和其他 AI 應用共用。

MCP 標準化了這件事。一個 MCP 伺服器會以清楚的說明和參數結構暴露工具。任何 MCP 用戶端都能連接、發現可用工具並使用它們。一次構建，到處使用。

下圖說明此架構——一個 MCP 用戶端（你的 AI 應用）同時連接多個 MCP 伺服器，每個伺服器都透過標準協議暴露自有工具集：

<img src="../../../translated_images/zh-TW/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型上下文協議架構 —— 標準化工具發現與執行*

## MCP 如何運作

MCP 底層採用分層架構。你的 Java 應用（MCP 用戶端）發現可用工具，透過傳輸層（Stdio 或 HTTP）發送 JSON-RPC 請求，MCP 伺服器執行操作並回傳結果。下圖拆解協議各層：

<img src="../../../translated_images/zh-TW/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 底層運作方式——用戶端發現工具、交換 JSON-RPC 訊息，並透過傳輸層執行操作。*

**伺服器-用戶端架構**

MCP 採用用戶端伺服器模型。伺服器提供工具——讀取檔案、查詢資料庫、調用 API。用戶端（你的 AI 應用）連接伺服器並使用其工具。

要在 LangChain4j 使用 MCP，可加入以下 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**工具發現**

當你的用戶端連接 MCP 伺服器時，它會詢問「你有哪些工具？」伺服器回應一份包含工具列表，每個帶描述和參數結構。你的 AI 代理根據用戶需求決定使用哪些工具。下圖顯示此交握過程——用戶端發送 `tools/list` 請求，伺服器回傳可用工具及其描述與參數結構：

<img src="../../../translated_images/zh-TW/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在啟動時發現可用工具——現在知道了哪些能力可用，並可決定使用哪個。*

**傳輸機制**

MCP 支援不同的傳輸機制。兩種選項是 Stdio（用於本地子程序通訊）和可串流 HTTP（用於遠端伺服器）。本模組示範 Stdio 傳輸：

<img src="../../../translated_images/zh-TW/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 傳輸機制：HTTP 用於遠端伺服器，Stdio 用於本地程序*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

適用本地程序。你的應用啟動一個子程序伺服器，並透過標準輸入輸出通訊。適合檔案系統存取或命令列工具。

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

`@modelcontextprotocol/server-filesystem` 伺服器暴露以下工具，並限定在你指定目錄下的沙盒環境：

| 工具 | 描述 |
|------|-------------|
| `read_file` | 讀取單一檔案內容 |
| `read_multiple_files` | 一次讀取多個檔案 |
| `write_file` | 創建或覆寫檔案 |
| `edit_file` | 針對性搜尋替換編輯 |
| `list_directory` | 列出路徑下檔案及目錄 |
| `search_files` | 遞迴搜尋符合模式的檔案 |
| `get_file_info` | 取得檔案元資料（大小、時間戳、權限） |
| `create_directory` | 建立目錄（含父目錄） |
| `move_file` | 移動或重新命名檔案或目錄 |

下圖展示 Stdio 傳輸的運作流程——你的 Java 應用啟動 MCP 伺服器做子程序，雙方透過 stdin/stdout 管道通訊，無需網路或 HTTP：

<img src="../../../translated_images/zh-TW/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 傳輸實況——應用程式作為母進程產生 MCP 子程序，並透過 stdin/stdout 管道溝通。*

> **🤖 嘗試透過 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) 並提問：
> - 「Stdio 傳輸如何運作？什麼時候該用它而非 HTTP？」
> - 「LangChain4j 如何管理起動的 MCP 伺服器程序生命週期？」
> - 「讓 AI 存取檔案系統，有哪些安全考量？」

## 代理模組

雖然 MCP 提供標準化工具，LangChain4j 的 **代理模組** 提供一種宣告性方式來建立協調這些工具的代理。`@Agent` 註解和 `AgenticServices` 讓你透過介面定義代理行為，而非命令式程式碼。

在本模組中，你將探索 **督導代理** 模式——一種先進的代理 AI 方法，其中「督導」代理根據用戶請求動態決定要呼叫哪些子代理。我們結合兩者概念，賦予其中一個子代理 MCP 驅動的檔案存取能力。

使用代理模組，加入以下 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **注意：** `langchain4j-agentic` 模組使用獨立版本屬性（`langchain4j.mcp.version`），因其發布排程與核心 LangChain4j 库不同步。

> **⚠️ 實驗性質：** `langchain4j-agentic` 模組為 **實驗性**，可能會改動。穩定建置 AI 助理的方式仍是使用帶自訂工具的 `langchain4j-core`（模組 04）。

## 執行範例

### 先決條件

- 完成[模組 04 - 工具](../04-tools/README.md)（本模組基於自訂工具概念，並與 MCP 工具比較）
- 根目錄存在帶 Azure 認證的 `.env` 檔（由模組 01 的 `azd up` 創建）
- Java 21+、Maven 3.9+
- Node.js 16+ 和 npm（用於 MCP 伺服器）

> **注意：** 若尚未設定環境變數，請參閱 [模組 01 - 介紹](../01-introduction/README.md) 的部署說明（`azd up` 會自動建立 `.env`），或在根目錄將 `.env.example` 複製為 `.env`，並填入你的設定值。

## 快速開始

**使用 VS Code：** 在檔案總管中右鍵單擊任一示範檔，選擇 **「執行 Java」**，或者使用「執行與除錯」面板中的啟動組態（確保 `.env` 檔先配置好 Azure 認證）。

**使用 Maven：** 也可從命令列使用以下示範執行。

### 檔案操作（Stdio）

示範基於本地子程序的工具。

**✅ 無需先決條件** — MCP 伺服器會自動啟動。

**使用啟動腳本（建議）：**

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

**使用 VS Code：** 右鍵 `StdioTransportDemo.java`，選「執行 Java」（確保 `.env` 已配置）。

應用程式自動啟動檔案系統 MCP 伺服器並讀取本地檔案。注意子程序管理已由程式處理。

**預期輸出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### 督導代理

**督導代理模式** 是一種 **彈性** 的代理 AI 形式。督導運用大型語言模型自主決定根據使用者請求應呼叫哪些代理。在下面的範例中，我們結合 MCP 驅動的檔案存取與 LLM 代理，建構監督式的讀檔案 → 報告工作流程。

示範中，`FileAgent` 透過 MCP 檔案系統工具讀取檔案，`ReportAgent` 產生結構化報告，包含執行摘要（一句話）、三個重點及建議。督導自動協調此流程：

<img src="../../../translated_images/zh-TW/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*督導以 LLM 決定呼叫哪些代理、順序，無需硬編碼路由。*

以下是我們檔案到報告流程的具體工作流程：

<img src="../../../translated_images/zh-TW/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 透過 MCP 工具讀取檔案，ReportAgent 將原始內容轉換成結構化報告。*

下圖是完整督導協調的序列圖——從啟動 MCP 伺服器、督導自主選擇代理、到透過 stdio 呼叫工具及產生最終報告：

<img src="../../../translated_images/zh-TW/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*督導自主呼叫 FileAgent（透過 stdio 呼叫 MCP 伺服器讀檔），再呼叫 ReportAgent 生成結構化報告——每個代理將輸出存放在共享的代理範圍（Agentic Scope）中。*

每個代理的輸出都儲存在 **代理範圍**（共享記憶體），讓下游代理能讀取之前結果。這展示 MCP 工具如何無縫整合進代理工作流程——督導不需知道讀檔細節，只需知道 `FileAgent` 能做到。

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

**使用 VS Code：** 右鍵 `SupervisorAgentDemo.java`，選「執行 Java」（確保 `.env` 已配置）。

#### 督導如何運作

建立代理之前，你需要連接 MCP 傳輸到用戶端並將其包裝成 `ToolProvider`。如此 MCP 伺服器的工具才能提供給你的代理使用：

```java
// 從傳輸建立 MCP 用戶端
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// 將用戶端包裝為 ToolProvider — 這將 MCP 工具橋接至 LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

接著你就能將 `mcpToolProvider` 注入任何需要 MCP 工具的代理：

```java
// 第一步：FileAgent 使用 MCP 工具讀取文件
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 擁有用於文件操作的 MCP 工具
        .build();

// 第二步：ReportAgent 產生結構化報告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// 監督者協調文件 → 報告工作流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 返回最終報告
        .build();

// 監督者根據請求決定調用哪些代理程式
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### FileAgent 如何在執行時發現 MCP 工具

你可能會問：**`FileAgent` 怎麼知道如何使用 npm 檔案系統工具？**答案是它不需要知道——由 **LLM** 透過工具結構在執行時推斷。
`FileAgent` 介面只是 **prompt 定義**。它沒有 `read_file`、`list_directory` 或任何其他 MCP 工具的硬編碼知識。以下說明端對端的流程：

1. **伺服器啟動：** `StdioMcpTransport` 啟動 `@modelcontextprotocol/server-filesystem` npm 套件作為子程序
2. **工具發現：** `McpClient` 傳送 `tools/list` JSON-RPC 請求到伺服器，伺服器回應工具名稱、說明與參數結構（例如：`read_file` — *「讀取檔案完整內容」* — `{ path: string }`）
3. **結構注入：** `McpToolProvider` 將這些發現的結構包裝起來，並提供給 LangChain4j 使用
4. **LLM 決策：** 當呼叫 `FileAgent.readFile(path)`，LangChain4j 將系統訊息、使用者訊息、**以及工具結構清單** 傳給 LLM。LLM 閱讀工具說明並產生工具呼叫（例如：`read_file(path="/some/file.txt")`）
5. **執行：** LangChain4j 攔截工具呼叫，通過 MCP 客戶端路由回 Node.js 子程序，取得結果後回傳給 LLM

這與上述描述的 [工具發現](../../../05-mcp) 機制相同，但特別應用於代理工作流程。`@SystemMessage` 和 `@UserMessage` 註解引導 LLM 行為，而注入的 `ToolProvider` 提供 **能力** — LLM 在執行時橋接這兩者。

> **🤖 試試 [GitHub Copilot](https://github.com/features/copilot) Chat：** 開啟 [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) 並詢問：
> - 「這個代理如何知道要呼叫哪個 MCP 工具？」
> - 「如果我從代理建構器移除 ToolProvider 會發生什麼？」
> - 「工具結構是如何傳給 LLM 的？」

#### 回應策略

設定 `SupervisorAgent` 時，你會指定當子代理完成任務後，要如何形成最終回答給使用者。下圖展示三種可用策略 — LAST 直接回傳最終代理輸出，SUMMARY 透過 LLM 摘要所有輸出，SCORED 則選擇對原始請求評分較高的答案：

<img src="../../../translated_images/zh-TW/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*監督者如何形成最終回應的三種策略 — 可依需求選擇最後代理輸出、綜合摘要或評分較高的選項。*

可用策略說明如下：

| 策略 | 說明 |
|----------|-------------|
| **LAST** | 監督者回傳最後被召喚的子代理或工具的輸出。適用於工作流程中最後一個代理專門產生完整最終答案的情況（例如研究流程中的「摘要代理」）。 |
| **SUMMARY** | 監督者使用自己的內部語言模型 (LLM) 將整個互動及所有子代理輸出做摘要，再回傳該摘要作為最終答案。提供簡潔且集合的回應。 |
| **SCORED** | 系統使用內部 LLM 對 LAST 回應與 SUMMARY 摘要，依原始使用者請求進行評分，回傳評分較高的輸出。 |

完整實作請參考 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)。

> **🤖 試試 [GitHub Copilot](https://github.com/features/copilot) Chat：** 開啟 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 並詢問：
> - 「監督者如何決定呼叫哪些代理？」
> - 「監督者和順序工作流程模式有何不同？」
> - 「我如何自訂監督者的規劃行為？」

#### 理解輸出結果

執行範例時，你會看到監督者如何協調多個代理的結構化流程說明。檔案各部分意義如下：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**標題**介紹工作流程概念：從讀取檔案到產生報告的聚焦管線。

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
  
**工作流程圖**展示代理間的資料流。每個代理負責特定任務：  
- **FileAgent** 使用 MCP 工具讀取檔案並將原始內容存入 `fileContent`  
- **ReportAgent** 使用該內容產生結構化報告並存入 `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**使用者請求** 展示任務內容。監督者解析後決定呼叫 FileAgent → ReportAgent。

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
  
**監督者編排** 展示兩步驟流程執行：  
1. **FileAgent** 通過 MCP 讀檔並保存內容  
2. **ReportAgent** 接收內容後產生結構化報告  

監督者根據使用者請求 **自主** 完成決策。

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

範例展示了 agentic 模組的多項進階功能。讓我們深入了解 Agentic 範圍 (Scope) 及 Agent 監聽器功能。

**Agentic 範圍** 是多個代理使用 `@Agent(outputKey="...")` 保存結果的共享記憶體。它允許：  
- 後續代理可讀取先前代理的輸出  
- 監督者合成最終回覆  
- 你檢查每個代理的產出  

下圖顯示 Agentic 範圍如何作為檔案到報告工作流程中的共享記憶體 — FileAgent 寫入 `fileContent`，ReportAgent 讀取它並寫入 `report`：

<img src="../../../translated_images/zh-TW/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic 範圍作為共享記憶體 — FileAgent 寫入 `fileContent`，ReportAgent 讀取後寫入 `report`，你可讀取最終結果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // 來自 FileAgent 的原始檔案資料
String report = scope.readState("report");            // 來自 ReportAgent 的結構化報告
```
  
**Agent 監聽器** 允許監控與偵錯代理執行。範例中所看到的逐步輸出來自監聽每次代理呼叫的 AgentListener：  
- **beforeAgentInvocation** — 當監督者選擇代理時觸發，可查看選哪個代理及原因  
- **afterAgentInvocation** — 代理執行完畢時觸發，顯示結果  
- **inheritedBySubagents** — 設為 true，監聽者會監控階層中所有代理  

下圖呈現完整 Agent 監聽器生命週期，包含在代理執行錯誤時如何由 `onError` 處理：

<img src="../../../translated_images/zh-TW/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent 監聽器掛鉤執行生命週期 — 監控代理啟動、完成或錯誤情況。*

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
  
除了監督者模式外，`langchain4j-agentic` 模組還提供多種強大工作流程模式。下圖呈現五種模式，從簡單順序管線到人員介入審核工作流程：

<img src="../../../translated_images/zh-TW/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*協調代理的五種工作流程模式 — 從簡單順序管線到人員介入的審核工作流程。*

| 模式 | 說明 | 使用範例 |
|---------|-------------|----------|
| **Sequential** | 依序執行代理，輸出流向下一個 | 管線：研究 → 分析 → 報告 |
| **Parallel** | 同時執行多個代理 | 獨立任務：天氣 + 新聞 + 股票 |
| **Loop** | 重複迭代直到條件滿足 | 質量評分：精煉直到分數 ≥ 0.8 |
| **Conditional** | 根據條件導向路由 | 分類 → 路由給專家代理 |
| **Human-in-the-Loop** | 新增人工審核節點 | 審核流程、內容審查 |

## 主要概念

現在你已實作 MCP 與 agentic 模組，來總結何時採用每種方案。

MCP 最大優勢之一是其不斷擴展的生態系統。下圖展示一個通用協議如何連結你的 AI 應用與各式 MCP 伺服器 — 從檔案系統和資料庫存取，到 GitHub、電子郵件、網路爬蟲等：

<img src="../../../translated_images/zh-TW/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 建立通用協議生態系統 — 任何 MCP 相容伺服器皆可與任何 MCP 相容客戶端搭配，實現跨應用共享工具。*

**MCP** 適合你想利用現有工具生態系、建置可由多應用共享的工具、整合第三方服務並使用標準協議，或想交換工具實作而不更改程式碼。

**Agentic 模組** 的優勢在於你需要用 `@Agent` 註解定義宣告式代理、需要工作流程協調（順序、迴圈、並行）、偏好介面導向代理設計而非命令式程式碼，或組合多個透過 `outputKey` 共享輸出的代理。

**監督者代理模式** 最適合流程無法預先規劃且需由 LLM 動態決策、多個專門代理需要動態協調、建構會話系統以路由至不同能力，或需最彈性適應的代理行為時。

以下比較可幫助你在模組 04 的自定義 `@Tool` 方法與本模組 MCP 工具間抉擇 — 自訂工具提供緊密耦合與完整型別安全以實現特定邏輯，MCP 工具則提供標準化且可重用整合：

<img src="../../../translated_images/zh-TW/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*何時使用自定義 @Tool 方法與 MCP 工具 — 自定工具適用應用特定邏輯且需完整型別安全，MCP 工具則適合標準化跨應用整合。*

## 恭喜！

你已完成 LangChain4j 初學者課程的所有五個模組！回顧你完成的完整學習旅程 — 從基礎聊天到 MCP 驅動的 agentic 系統：

<img src="../../../translated_images/zh-TW/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*從基礎聊天到 MCP 支援的 agentic 系統的完整學習旅程。*

你學會了：

- 如何打造具有記憶功能的對話 AI（模組 01）
- 不同任務的 prompt 工程模式（模組 02）
- 利用 RAG 將回應結合文件基礎（模組 03）
- 以自定義工具構建基礎 AI 代理（助手）（模組 04）
- 透過 LangChain4j MCP 和 Agentic 模組整合標準化工具（模組 05）

### 接下來呢？

完成模組後，請參考 [測試指南](../docs/TESTING.md) 了解 LangChain4j 測試概念實作。

**官方資源：**  
- [LangChain4j 文件](https://docs.langchain4j.dev/) — 全面指南與 API 參考  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) — 原始碼與範例  
- [LangChain4j 教學](https://docs.langchain4j.dev/tutorials/) — 步驟式教學，涵蓋各種應用場景  

感謝你完成本課程！

---

**導航：** [← 上一節：模組 04 - 工具](../04-tools/README.md) | [回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯完成。雖然我們致力於追求準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應被視為權威版本。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生之任何誤解或曲解負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->