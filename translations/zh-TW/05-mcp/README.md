# Module 05: 模型上下文協議 (MCP)

## 目錄

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
    - [回應策略](../../../05-mcp)
    - [理解輸出結果](../../../05-mcp)
    - [Agentic 模組功能說明](../../../05-mcp)
- [關鍵概念](../../../05-mcp)
- [恭喜！](../../../05-mcp)
  - [接下來呢？](../../../05-mcp)

## 你將學到什麼

你已經建立了會話式 AI，掌握了提示語，讓回答依據文件為基礎，並創建了具備工具的代理。但這些工具都是為你的特定應用量身訂做的。如果你能讓你的 AI 訪問一個標準化的工具生態系，而任何人都能建立並分享這些工具，該多好？在本模組中，你將學習如何使用模型上下文協議 (MCP) 和 LangChain4j 的 agentic 模組來做到這一點。我們首先展示一個簡單的 MCP 檔案閱讀器，然後展示它如何輕鬆整合進使用監督代理模式的進階 agentic 工作流程。

## 什麼是 MCP？

模型上下文協議 (MCP) 正是提供了這樣一個標準化的方式，讓 AI 應用程式能夠發現並使用外部工具。你不需要為每個資料來源或服務撰寫客製整合程式，而是連接到以一致格式公開其功能的 MCP 伺服器。你的 AI 代理即可自動發現並使用這些工具。

<img src="../../../translated_images/zh-TW/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*在 MCP 出現之前：複雜的點對點整合。MCP 出現之後：一個協議，無限可能。*

MCP 解決 AI 開發中的根本問題：每個整合都是客製。想存取 GitHub？寫客製程式。想讀文件？寫客製程式。想查詢資料庫？寫客製程式。這些整合彼此間都不相容。

MCP 使之標準化。MCP 伺服器公開帶有明確描述和結構的工具，任何 MCP 用戶端都能連接、發現可用工具並使用。寫一次，處處可用。

<img src="../../../translated_images/zh-TW/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型上下文協議架構－標準化的工具發現與執行*

## MCP 如何運作

<img src="../../../translated_images/zh-TW/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 實作細節－用戶端發現工具、交換 JSON-RPC 訊息，並透過傳輸層執行操作。*

**伺服器－用戶端架構**

MCP 採用用戶端伺服器模型。伺服器提供工具－讀文件、查詢資料庫、呼叫 API 等。用戶端（你的 AI 應用）連接伺服器並使用該工具。

要在 LangChain4j 中使用 MCP，加入以下 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```


**工具發現**

當你的用戶端連接 MCP 伺服器時，會詢問「你有什麼工具？」伺服器回應可用工具清單，每個工具附帶描述和參數結構。你的 AI 代理可根據使用者需求決定使用哪些工具。

<img src="../../../translated_images/zh-TW/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在啟動時發現可用工具－它因此知道有哪些功能可用，且可決定使用哪些。*

**傳輸機制**

MCP 支援多種傳輸機制。本模組展示用於本地程序的 Stdio 傳輸：

<img src="../../../translated_images/zh-TW/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 傳輸機制：遠端伺服器用 HTTP，本地程序用 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

適用本地程序。你的應用啟動伺服器作為子程序，透過標準輸入輸出通訊。適合檔案系統存取或命令列工具。

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


<img src="../../../translated_images/zh-TW/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 傳輸示意－你的應用啟動 MCP 伺服器子程序，透過 stdin/stdout 管線溝通。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天體驗：** 開啟 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) 並詢問：
> - 「Stdio 傳輸如何運作？什麼時候該用它，而不是 HTTP？」
> - 「LangChain4j 怎麼管理啟動的 MCP 伺服器程序生命週期？」
> - 「讓 AI 存取檔案系統有什麼安全風險？」

## Agentic 模組

雖然 MCP 提供標準化工具，LangChain4j 的 **agentic 模組** 則提供一種聲明式方式打造代理，協調這些工具。`@Agent` 註解和 `AgenticServices` 讓你透過介面定義代理行為，而非命令式程式碼。

在本模組中，你將探索 **監督代理** 模式－一種進階 agentic AI 方法，讓一個「監督」代理根據使用者需求動態決定召喚哪些子代理。我們會將 MCP 支援的檔案存取能力整合給其中一個子代理。

要使用 agentic 模組，加入以下 Maven 依賴：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```


> **⚠️ 實驗性功能：** `langchain4j-agentic` 模組是 **實驗性**，未來可能變動。建議穩定建立 AI 助理仍以 `langchain4j-core` 搭配自訂工具（模組 04）。

## 執行範例

### 先決條件

- Java 21+、Maven 3.9+
- Node.js 16+ 和 npm（用於 MCP 伺服器）
- 根目錄 `.env` 檔配置環境變數：
  - `AZURE_OPENAI_ENDPOINT`、`AZURE_OPENAI_API_KEY`、`AZURE_OPENAI_DEPLOYMENT`（與模組 01-04 相同）

> **注意：** 若尚未設定環境變數，請參閱 [模組 00 - 快速開始](../00-quick-start/README.md) 指示，或從根目錄複製 `.env.example` 為 `.env` 並填入你的值。

## 快速開始

**使用 VS Code:** 在檔案總管中右鍵點擊任一示範檔案，選擇 **「執行 Java」**，或從執行與除錯面板使用啟動設定（請先將 token 加入 `.env` 檔）。

**使用 Maven:** 你也可以從命令列執行以下範例。

### 檔案操作（Stdio）

示範以本地子程序為基礎的工具。

**✅ 不需先決條件** - MCP 伺服器會自動啟動。

**使用啟動腳本（建議）:**

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

**使用 VS Code:** 右鍵點擊 `StdioTransportDemo.java` ，選擇 **「執行 Java」**（請確保 `.env` 已設定）。

應用自動啟動一個檔案系統 MCP 伺服器並讀取本地檔案。注意子程序管理由系統代為處理。

**預期輸出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```


### 監督代理

**監督代理模式** 是 agentic AI 的一種 **彈性** 形式。監督使用 LLM 自主決定根據使用者需求召喚哪些代理。下一個範例中，我們結合 MCP 支援的檔案存取和 LLM 代理，創建一個從檔案讀取到報告產出的監督流程。

示範中，`FileAgent` 使用 MCP 檔案系統工具讀取檔案，`ReportAgent` 生成結構化報告（包含一句執行摘要、三個重點及建議）。監督自動協調此流程：

<img src="../../../translated_images/zh-TW/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*監督利用其 LLM 決定召喚哪些代理以及順序－不需硬編碼路由。*

具體工作流程如下：

<img src="../../../translated_images/zh-TW/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 透過 MCP 工具讀檔，ReportAgent 將原始內容轉換為結構化報告。*

每個代理將輸出存放於 **Agentic Scope**（共享記憶體），允許後續代理存取先前結果。這說明 MCP 工具如何無縫整合到 agentic 工作流程－監督不需知道檔案怎麼讀，只知道 `FileAgent` 可以做到。

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

**使用 VS Code:** 右鍵點擊 `SupervisorAgentDemo.java` ，選擇 **「執行 Java」**（確保 `.env` 已設定）。

#### 監督如何運作

```java
// 第一步：FileAgent 使用 MCP 工具讀取檔案
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 具有檔案操作的 MCP 工具
        .build();

// 第二步：ReportAgent 產生結構化報告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor 協調檔案→報告的工作流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 回傳最終報告
        .build();

// Supervisor 根據請求決定要呼叫哪些代理人
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```


#### 回應策略

設定 `SupervisorAgent` 時，你會指定在子代理完成任務後，監督如何擬定對使用者的最終回覆。

<img src="../../../translated_images/zh-TW/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*監督擬定最終回應的三種策略－根據你想要最後代理輸出、綜合摘要或最佳分數選擇。*

可用策略有：

| 策略 | 說明 |
|----------|-------------|
| **LAST** | 監督回傳最後呼叫子代理或工具的輸出。適用於流程中最後一個代理專門產生完整最終答案時（例如研究流程中的「摘要代理」）。 |
| **SUMMARY** | 監督使用自己的內部語言模型 (LLM) 將整體互動與所有子代理輸出綜合成摘要，並回傳此摘要作為最終回應。提供給使用者一個清晰彙整的答案。 |
| **SCORED** | 系統用內部 LLM 對 LAST 回應與 SUMMARY 回應就原始使用者請求進行評分，返回評分較高的輸出。 |

完整實做請參考 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天體驗：** 開啟 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 並詢問：
> - 「監督如何決定要召喚哪些代理？」
> - 「監督與逐步流程模式有什麼差異？」
> - 「如何自訂監督的規劃行為？」

#### 理解輸出結果

執行示範時，你會看到監督如何協調多個代理的結構化流程。以下解釋每個區塊的含義：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**標題**介紹工作流程概念：從讀檔到報告生成的專注流程。

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

**流程圖**展示代理間的資料流。每個代理擔任特定角色：
- **FileAgent** 利用 MCP 工具讀檔並將原始內容存入 `fileContent`
- **ReportAgent** 消耗內容並產生結構化報告於 `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**使用者請求**展示任務內容。監督解析後決定依序召喚 FileAgent → ReportAgent。

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

**監督協調**展示兩步驟流程實作：
1. **FileAgent** 經 MCP 讀檔並存內容
2. **ReportAgent** 接收內容並生成結構化報告

監督基於使用者請求**自主**做出這些決策。

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

範例展示了 agentic 模組的多項進階功能。我們來深入看看 Agentic Scope 和 Agent Listeners。

**Agentic Scope** 顯示代理使用 `@Agent(outputKey="...")` 儲存結果的共享記憶體。這允許：
- 後續代理取用先行代理輸出
- 監督綜合產生最終回應
- 你檢查每個代理生成的內容

<img src="../../../translated_images/zh-TW/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope 作為共享記憶體－FileAgent 寫入 `fileContent`，ReportAgent 讀取並寫入 `report`，你的程式碼讀取最終結果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // 來自 FileAgent 的原始檔案資料
String report = scope.readState("report");            // 來自 ReportAgent 的結構化報告
```


**Agent Listeners** 允許監控和偵錯代理執行。你在示範中看到的逐步輸出，來自鉤掛在每次代理呼叫的 AgentListener：*
- **beforeAgentInvocation** - 當監督者選擇代理時調用，讓您看到選擇了哪個代理以及原因
- **afterAgentInvocation** - 當代理完成時調用，顯示其結果
- **inheritedBySubagents** - 為 true 時，監聽器監控整個階層中的所有代理

<img src="../../../translated_images/zh-TW/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*代理監聽器掛載於執行生命週期 — 監控代理何時開始、完成或遇到錯誤。*

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

除了監督者模式外，`langchain4j-agentic` 模組還提供了多種強大的工作流程模式和功能：

<img src="../../../translated_images/zh-TW/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*五種管理代理的工作流程模式 — 從簡單的串接管線到人員審核流程。*

| 模式 | 說明 | 使用情境 |
|---------|-------------|----------|
| **Sequential** | 按順序執行代理，輸出流向下一個 | 管線：研究 → 分析 → 報告 |
| **Parallel** | 同時執行多個代理 | 獨立任務：天氣 + 新聞 + 股票 |
| **Loop** | 迭代直到達成條件 | 品質評分：修正直到分數 ≥ 0.8 |
| **Conditional** | 根據條件路由 | 分類 → 轉給專家代理 |
| **Human-in-the-Loop** | 加入人工檢查點 | 審核流程、內容審查 |

## 重要概念

現在您已經體驗了 MCP 與 agentic 模組的運作，讓我們總結各自適合的使用時機。

<img src="../../../translated_images/zh-TW/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 建立了通用協定生態系 — 任何 MCP 相容的伺服器都可與任何 MCP 相容的客戶端通訊，促進工具在各應用中的共享。*

**MCP** 適合您想利用既有工具生態系、建立多應用共享的工具、結合標準協定的第三方服務，或想在不改變程式的情況下切換工具實作時。

**Agentic 模組** 最適合希望透過 `@Agent` 註解做宣告式代理定義，需要工作流程編排（順序、迴圈、並行），偏好介面導向代理設計而非指令式程式碼，或多個代理共享輸出 (outputKey) 。

**監督者代理模式** 在工作流程事先不可預測、想讓 LLM 自主決定，擁有多專業代理需要動態編排，建立會話系統導向不同能力，或需要最靈活、適應性最高的代理行為時最為出色。

<img src="../../../translated_images/zh-TW/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*何時使用自訂的 @Tool 方法對比 MCP 工具 — 自訂工具適合應用特定邏輯且具完整型別安全，MCP 工具適合跨應用的標準整合。*

## 恭喜！

<img src="../../../translated_images/zh-TW/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*您完成了五個模組的學習之旅 — 從基本聊天到 MCP 驅動的 agentic 系統。*

您已完成 LangChain4j 初學者課程。您學到：

- 如何建構具備記憶的對話式 AI（模組 01）
- 適用於不同任務的提示工程模式（模組 02）
- 透過 RAG 將回應依據文件做語境依據（模組 03）
- 使用自訂工具建立基礎 AI 代理（助手）（模組 04）
- 使用 LangChain4j MCP 與 Agentic 模組整合標準化工具（模組 05）

### 下一步？

完成模組後，探索 [Testing Guide](../docs/TESTING.md) 了解 LangChain4j 測試概念的實作。

**官方資源：**
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - 全面指南與 API 參考
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - 原始碼與範例
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - 各種使用案例分步教學

感謝您完成本課程！

---

**導航：** [← 上一頁：模組 04 - 工具](../04-tools/README.md) | [回主頁](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保翻譯的準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於關鍵資訊，建議仍聘請專業人工翻譯。我們不對因使用此翻譯而產生的任何誤解或曲解負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->