# LangChain4j 詞彙表

## 目錄

- [核心概念](../../../docs)
- [LangChain4j 元件](../../../docs)
- [AI/ML 概念](../../../docs)
- [安全守則](../../../docs)
- [提示工程](../../../docs)
- [RAG（檢索增強生成）](../../../docs)
- [代理人與工具](../../../docs)
- [Agentic 模組](../../../docs)
- [模型上下文協議（MCP）](../../../docs)
- [Azure 服務](../../../docs)
- [測試與開發](../../../docs)

快速參考課程中使用的術語與概念。

## 核心概念

**AI 代理人** - 使用 AI 進行推理並自主行動的系統。[模組 04](../04-tools/README.md)

**鏈（Chain）** - 輸出作為下一步輸入的操作序列。

**分塊（Chunking）** - 將文件切割成較小片段。典型大小：300-500 個標記且有重疊。[模組 03](../03-rag/README.md)

**上下文視窗（Context Window）** - 模型能處理的最大標記數。GPT-5.2：400K 標記（最高272K 輸入，128K 輸出）。

**嵌入（Embeddings）** - 表示文本語意的數值向量。[模組 03](../03-rag/README.md)

**函數呼叫（Function Calling）** - 模型生成結構化請求來調用外部函數。[模組 04](../04-tools/README.md)

**幻覺（Hallucination）** - 當模型生成錯誤但看似合理的資訊。

**提示（Prompt）** - 輸入給語言模型的文本。[模組 02](../02-prompt-engineering/README.md)

**語意搜尋（Semantic Search）** - 透過語意嵌入而非關鍵字進行搜尋。[模組 03](../03-rag/README.md)

**有狀態與無狀態（Stateful vs Stateless）** - 無狀態：無記憶。有狀態：維持對話歷史。[模組 01](../01-introduction/README.md)

**標記（Tokens）** - 模型處理的基本文字單位。影響成本和限制。[模組 01](../01-introduction/README.md)

**工具鏈接（Tool Chaining）** - 按順序執行工具，其中輸出用於下一次呼叫。[模組 04](../04-tools/README.md)

## LangChain4j 元件

**AiServices** - 創建類型安全的 AI 服務介面。

**OpenAiOfficialChatModel** - 統一用戶端，支援 OpenAI 和 Azure OpenAI 模型。

**OpenAiOfficialEmbeddingModel** - 使用 OpenAI 官方用戶端製作嵌入（同時支援 OpenAI 與 Azure OpenAI）。

**ChatModel** - 語言模型的核心介面。

**ChatMemory** - 維護對話歷史。

**ContentRetriever** - 找出 RAG 相關的文件片段。

**DocumentSplitter** - 將文件拆成多個片段。

**EmbeddingModel** - 將文本轉換為數值向量。

**EmbeddingStore** - 存取並檢索嵌入。

**MessageWindowChatMemory** - 維持最近消息的滑動視窗。

**PromptTemplate** - 使用 `{{variable}}` 佔位符建立可重用提示。

**TextSegment** - 附帶元資料的文本片段。在 RAG 中使用。

**ToolExecutionRequest** - 工具執行請求物件。

**UserMessage / AiMessage / SystemMessage** - 對話訊息類型。

## AI/ML 概念

**少量示例學習（Few-Shot Learning）** - 在提示中提供範例。[模組 02](../02-prompt-engineering/README.md)

**大型語言模型（LLM）** - 以大量文本資料訓練的 AI 模型。

**推理深度（Reasoning Effort）** - GPT-5.2 用以控制思考深度的參數。[模組 02](../02-prompt-engineering/README.md)

**溫度（Temperature）** - 控制輸出隨機性。低=確定性，高=創造性。

**向量資料庫（Vector Database）** - 專為嵌入設計的資料庫。[模組 03](../03-rag/README.md)

**零示範學習（Zero-Shot Learning）** - 無範例執行任務。[模組 02](../02-prompt-engineering/README.md)

## 安全守則 - [模組 00](../00-quick-start/README.md)

**多重防禦（Defense in Depth）** - 多層安全策略，結合應用層守則與供應商安全過濾器。

**硬阻擋（Hard Block）** - 嚴重內容違規時供應商回傳 HTTP 400 錯誤。

**InputGuardrail** - LangChain4j 介面，用於在提示送入 LLM 前驗證用戶輸入，提早阻擋有害提示，節省成本和延遲。

**InputGuardrailResult** - 守則驗證回傳類型：`success()` 或 `fatal("原因")`。

**OutputGuardrail** - 驗證 AI 回應介面，防止不當內容回傳給用戶。

**供應商安全過濾器** - AI 供應商內建 API 層的內容過濾（例如 GitHub Models）。

**軟拒絕（Soft Refusal）** - 模型禮貌拒絕回答，但不引發錯誤。

## 提示工程 - [模組 02](../02-prompt-engineering/README.md)

**思路鏈（Chain-of-Thought）** - 逐步推理以提升準確度。

**約束輸出（Constrained Output）** - 強制特定格式或結構。

**高度求解度（High Eagerness）** - GPT-5.2 模式，重度推理。

**低度求解度（Low Eagerness）** - GPT-5.2 模式，快速回答。

**多輪對話（Multi-Turn Conversation）** - 保持跨輪上下文。

**基於角色的提示（Role-Based Prompting）** - 透過系統訊息設定模型角色。

**自我反省（Self-Reflection）** - 模型評估並改進自己的輸出。

**結構化分析（Structured Analysis）** - 固定評估框架。

**任務執行模式（Task Execution Pattern）** - 計畫 → 執行 → 總結。

## RAG（檢索增強生成）- [模組 03](../03-rag/README.md)

**文件處理管線** - 載入 → 分塊 → 嵌入 → 儲存。

**記憶體內嵌入庫** - 非持久性儲存，用於測試。

**RAG** - 結合檢索與生成，以使回應有依據。

**相似度分數** - 表示語意相似度的度量（0-1）。

**來源引用** - 檢索內容的元資料。

## 代理人與工具 - [模組 04](../04-tools/README.md)

**@Tool 標註** - 將 Java 方法標記為 AI 可呼叫工具。

**ReAct 模式** - 推理 → 行動 → 觀察 → 重複。

**會話管理** - 不同用戶使用分離上下文。

**工具（Tool）** - AI 代理人可呼叫的功能。

**工具說明** - 工具目的與參數文件。

## Agentic 模組 - [模組 05](../05-mcp/README.md)

**@Agent 標註** - 將介面標記為 AI 代理人，使用宣告式行為定義。

**代理人監聽器（Agent Listener）** - 透過 `beforeAgentInvocation()` 和 `afterAgentInvocation()` 鉤子監控代理人執行。

**Agentic 範圍** - 代理人存放輸出供下游代理人使用的共用記憶體，使用 `outputKey` 參數指定位置。

**AgenticServices** - 用於透過 `agentBuilder()` 和 `supervisorBuilder()` 創建代理人的工廠。

**條件工作流程（Conditional Workflow）** - 根據條件導向不同專家代理人。

**人機協作（Human-in-the-Loop）** - 增加人工審核或內容檢查節點的工作流程模式。

**langchain4j-agentic** - 用於宣告式代理構建的 Maven 依賴（實驗性）。

**循環工作流程（Loop Workflow）** - 反覆執行代理人直到達成條件（例如品質分數 ≥ 0.8）。

**outputKey** - 代理人註解參數，指定結果儲存於 Agentic 範圍的位置。

**並行工作流程（Parallel Workflow）** - 同時執行多個獨立任務代理人。

**回應策略（Response Strategy）** - 監督者制定最終答案的方式：LAST、SUMMARY 或 SCORED。

**連續工作流程（Sequential Workflow）** - 按順序執行代理人，輸出流向下一步。

**監督代理人模式（Supervisor Agent Pattern）** - 進階 agentic 模式，由監督者 LLM 動態決定呼叫哪些子代理人。

## 模型上下文協議（MCP）- [模組 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j 中整合 MCP 的 Maven 依賴。

**MCP** - 模型上下文協議：連接 AI 應用到外部工具的標準，開發一次多處使用。

**MCP 用戶端** - 連接 MCP 伺服器，發現並使用工具的應用程式。

**MCP 伺服器** - 以 MCP 協議公開工具，附有清晰描述與參數模式的服務。

**McpToolProvider** - LangChain4j 元件，包裝 MCP 工具以供 AI 服務和代理人使用。

**McpTransport** - MCP 通訊介面，實作包含 Stdio 與 HTTP。

**Stdio 傳輸** - 透過 stdin/stdout 的本地程序傳輸，適用於檔案系統存取或命令列工具。

**StdioMcpTransport** - LangChain4j 實作，將 MCP 伺服器作為子程序啟動。

**工具發現（Tool Discovery）** - 用戶端查詢伺服器取得可用工具的描述與模式。

## Azure 服務 - [模組 01](../01-introduction/README.md)

**Azure AI Search** - 具備向量能力的雲端搜尋。[模組 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - 部署 Azure 資源。

**Azure OpenAI** - 微軟企業級 AI 服務。

**Bicep** - Azure 基礎架構即程式碼語言。[基礎架構指南](../01-introduction/infra/README.md)

**部署名稱** - Azure 中模型部署的名稱。

**GPT-5.2** - 最新 OpenAI 模型，具推理控制能力。[模組 02](../02-prompt-engineering/README.md)

## 測試與開發 - [測試指南](TESTING.md)

**開發容器（Dev Container）** - 容器化開發環境。[設定檔](../../../.devcontainer/devcontainer.json)

**GitHub Models** - 免費 AI 模型試驗場。[模組 00](../00-quick-start/README.md)

**記憶體內測試** - 使用記憶體儲存的測試。

**整合測試（Integration Testing）** - 使用真實基礎架構的測試。

**Maven** - Java 自動建構工具。

**Mockito** - Java 模擬測試框架。

**Spring Boot** - Java 應用框架。[模組 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件乃使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。儘管我們致力於確保準確性，但請注意自動翻譯可能存在錯誤或不準確之處。原文應以其母語版本為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用此翻譯而引起的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->