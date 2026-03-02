# LangChain4j 詞彙表

## 目錄

- [核心概念](../../../docs)
- [LangChain4j 組件](../../../docs)
- [AI/ML 概念](../../../docs)
- [防護措施](../../../docs)
- [提示工程](../../../docs)
- [RAG（檢索增強生成）](../../../docs)
- [代理與工具](../../../docs)
- [代理模組](../../../docs)
- [模型上下文協議 (MCP)](../../../docs)
- [Azure 服務](../../../docs)
- [測試與開發](../../../docs)

課程中使用的術語和概念快速參考。

## 核心概念

**AI Agent** - 使用 AI 進行推理和自主行動的系統。 [模組 04](../04-tools/README.md)

**Chain** - 一系列操作，輸出作為下一步輸入。

**Chunking** - 將文件分割成較小片段。典型大小：300-500 代幣，帶有重疊。 [模組 03](../03-rag/README.md)

**Context Window** - 模型可處理的最大代幣數。GPT-5.2：400K 代幣（最多 272K 輸入，128K 輸出）。

**Embeddings** - 表示文本含義的數值向量。 [模組 03](../03-rag/README.md)

**Function Calling** - 模型生成結構化請求以調用外部函數。 [模組 04](../04-tools/README.md)

**Hallucination** - 模型生成錯誤但似是而非的信息。

**Prompt** - 語言模型的文本輸入。 [模組 02](../02-prompt-engineering/README.md)

**Semantic Search** - 使用嵌入向量按意義搜尋，而非關鍵詞。 [模組 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless：無記憶。Stateful：保留對話歷史。 [模組 01](../01-introduction/README.md)

**Tokens** - 模型處理的基本文本單位。影響成本與限制。 [模組 01](../01-introduction/README.md)

**Tool Chaining** - 工具依序執行，輸出用於下一次調用。 [模組 04](../04-tools/README.md)

## LangChain4j 組件

**AiServices** - 建立類型安全的 AI 服務介面。

**OpenAiOfficialChatModel** - OpenAI 與 Azure OpenAI 模型的統一客戶端。

**OpenAiOfficialEmbeddingModel** - 使用 OpenAI 官方客戶端建立嵌入（支援 OpenAI 與 Azure OpenAI）。

**ChatModel** - 語言模型核心介面。

**ChatMemory** - 保持會話歷史。

**ContentRetriever** - 尋找 RAG 相關的文件片段。

**DocumentSplitter** - 將文件拆分成片段。

**EmbeddingModel** - 將文本轉換為數值向量。

**EmbeddingStore** - 儲存與檢索嵌入。

**MessageWindowChatMemory** - 維持近期訊息的滑動視窗。

**PromptTemplate** - 建立可重用提示，包含 `{{variable}}` 佔位符。

**TextSegment** - 帶有元數據的文本塊，用於 RAG。

**ToolExecutionRequest** - 代表工具執行請求。

**UserMessage / AiMessage / SystemMessage** - 會話消息類型。

## AI/ML 概念

**Few-Shot Learning** - 在提示中提供範例。 [模組 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - 以大量文本數據訓練的 AI 模型。

**Reasoning Effort** - GPT-5.2 控制推理深度的參數。 [模組 02](../02-prompt-engineering/README.md)

**Temperature** - 控制輸出隨機性。低溫＝決定性，高溫＝創造性。

**Vector Database** - 專為嵌入設計的資料庫。 [模組 03](../03-rag/README.md)

**Zero-Shot Learning** - 無範例執行任務。 [模組 02](../02-prompt-engineering/README.md)

## 防護措施 - [模組 00](../00-quick-start/README.md)

**Defense in Depth** - 多層次安全策略，結合應用層防護與供應商安全過濾。

**Hard Block** - 供應商對嚴重違規內容直接回傳 HTTP 400 錯誤。

**InputGuardrail** - LangChain4j 介面，用於在輸入到 LLM 前驗證用戶輸入。透過早期封鎖惡意提示節省成本與延遲。

**InputGuardrailResult** - 防護檢查返回類型：`success()` 或 `fatal("原因")`。

**OutputGuardrail** - 驗證 AI 回應後再返回給用戶的介面。

**Provider Safety Filters** - AI 供應商（如 GitHub Models）內建內容過濾器，在 API 層阻擋違規。

**Soft Refusal** - 模型禮貌拒絕回答但不返回錯誤。

## 提示工程 - [模組 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - 逐步推理以提升準確度。

**Constrained Output** - 強制特定格式或結構。

**High Eagerness** - GPT-5.2 模式，要求徹底推理。

**Low Eagerness** - GPT-5.2 模式，快速回答。

**Multi-Turn Conversation** - 談話串保持上下文。

**Role-Based Prompting** - 透過系統消息設定模型角色。

**Self-Reflection** - 模型自我評估並改進輸出。

**Structured Analysis** - 固定評估框架。

**Task Execution Pattern** - 計畫 → 執行 → 總結。

## RAG（檢索增強生成） - [模組 03](../03-rag/README.md)

**Document Processing Pipeline** - 載入 → 切割 → 嵌入 → 儲存。

**In-Memory Embedding Store** - 用於測試的非持久儲存。

**RAG** - 結合檢索與生成以增強回答可靠性。

**Similarity Score** - 語義相似度量（0-1）。

**Source Reference** - 檢索內容的元資料。

## 代理與工具 - [模組 04](../04-tools/README.md)

**@Tool Annotation** - 標註 Java 方法為 AI 可調用工具。

**ReAct Pattern** - 推理 → 行動 → 觀察 → 重複。

**Session Management** - 為不同用戶區分上下文。

**Tool** - AI 代理可調用的功能。

**Tool Description** - 工具功能與參數文件。

## 代理模組 - [模組 05](../05-mcp/README.md)

**@Agent Annotation** - 標註介面為具宣告式行為定義的 AI 代理。

**Agent Listener** - 透過 `beforeAgentInvocation()` 和 `afterAgentInvocation()` 監控代理執行的掛勾。

**Agentic Scope** - 代理共享記憶區，代理使用 `outputKey` 儲存輸出給下游代理使用。

**AgenticServices** - 建立代理的工廠，提供 `agentBuilder()` 與 `supervisorBuilder()`。

**Conditional Workflow** - 根據條件路由至不同專家代理。

**Human-in-the-Loop** - 工作流加入人工審核或內容審查節點。

**langchain4j-agentic** - 宣告式代理工廠的 Maven 依賴（實驗性質）。

**Loop Workflow** - 持續迭代代理執行直到滿足條件（如質量分數 ≥ 0.8）。

**outputKey** - 代理註解參數，指定結果存放於 Agentic Scope 的位置。

**Parallel Workflow** - 同時執行多個獨立任務代理。

**Response Strategy** - 主管如何形成最終回答策略：LAST、SUMMARY 或 SCORED。

**Sequential Workflow** - 依序執行代理，輸出作為下一步輸入。

**Supervisor Agent Pattern** - 進階代理模式，主管 LLM 動態決定調用哪些子代理。

## 模型上下文協議 (MCP) - [模組 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j 中 MCP 整合的 Maven 依賴。

**MCP** - 模型上下文協議：連接 AI 應用與外部工具的標準。一次構建，到處使用。

**MCP Client** - 連接 MCP 伺服器，發現並使用工具的應用。

**MCP Server** - 透過 MCP 提供工具的服務，附帶清晰描述與參數結構。

**McpToolProvider** - LangChain4j 元件，包裝 MCP 工具供 AI 服務與代理使用。

**McpTransport** - MCP 通信介面，實現包括 Stdio 和 HTTP。

**Stdio Transport** - 透過 stdin/stdout 的本地流程傳輸。適用於存取文件系統或命令列工具。

**StdioMcpTransport** - LangChain4j 實現，啟動 MCP 伺服器子程序。

**Tool Discovery** - 客戶端查詢伺服器可用工具及其描述與結構。

## Azure 服務 - [模組 01](../01-introduction/README.md)

**Azure AI Search** - 支援向量功能的雲端搜尋服務。 [模組 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - 部署 Azure 資源工具。

**Azure OpenAI** - 微軟的企業級 AI 服務。

**Bicep** - Azure 基礎架構即代碼語言。 [基礎設施指南](../01-introduction/infra/README.md)

**Deployment Name** - Azure 中模型部署的名稱。

**GPT-5.2** - 最新 OpenAI 模型，具備推理控制能力。 [模組 02](../02-prompt-engineering/README.md)

## 測試與開發 - [測試指南](TESTING.md)

**Dev Container** - 容器化開發環境。 [設定](../../../.devcontainer/devcontainer.json)

**GitHub Models** - 免費 AI 模型試驗場。 [模組 00](../00-quick-start/README.md)

**In-Memory Testing** - 使用記憶體儲存執行測試。

**Integration Testing** - 使用真實基礎設施進行測試。

**Maven** - Java 建構自動化工具。

**Mockito** - Java 模擬框架。

**Spring Boot** - Java 應用框架。 [模組 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件是使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們努力確保翻譯的準確性，但請注意，機器自動翻譯可能會包含錯誤或不準確之處。原始語言版本的文件應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用此翻譯而引起的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->