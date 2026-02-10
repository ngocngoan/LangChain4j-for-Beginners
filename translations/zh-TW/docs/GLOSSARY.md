# LangChain4j 詞彙表

## 目錄

- [核心概念](../../../docs)
- [LangChain4j 元件](../../../docs)
- [AI/ML 概念](../../../docs)
- [防護措施](../../../docs)
- [提示工程](../../../docs)
- [檢索增強生成 (RAG)](../../../docs)
- [代理和工具](../../../docs)
- [代理式模組](../../../docs)
- [模型上下文協議 (MCP)](../../../docs)
- [Azure 服務](../../../docs)
- [測試與開發](../../../docs)

課程中使用術語和概念的快速參考。

## 核心概念

**AI Agent** - 使用 AI 自主推理和行動的系統。 [模組 04](../04-tools/README.md)

**Chain** - 一系列操作，輸出作為下一步的輸入。

**Chunking** - 將文件分割成較小部分。典型大小：300-500 個標記，有重疊。 [模組 03](../03-rag/README.md)

**Context Window** - 模型能處理的最大標記數。GPT-5.2：400K 個標記。

**Embeddings** - 代表文字意義的數值向量。 [模組 03](../03-rag/README.md)

**Function Calling** - 模型生成結構化請求以呼叫外部函式。 [模組 04](../04-tools/README.md)

**Hallucination** - 模型生成不正確但看似合理的資訊。

**Prompt** - 輸入給語言模型的文字。 [模組 02](../02-prompt-engineering/README.md)

**Semantic Search** - 使用 embeddings 依語義而非關鍵字搜尋。 [模組 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless：無記憶。Stateful：維持對話歷史。 [模組 01](../01-introduction/README.md)

**Tokens** - 模型處理的基本文字單位。影響成本與限制。 [模組 01](../01-introduction/README.md)

**Tool Chaining** - 以輸出決定下一步呼叫的連續工具執行。 [模組 04](../04-tools/README.md)

## LangChain4j 元件

**AiServices** - 創建型別安全的 AI 服務介面。

**OpenAiOfficialChatModel** - OpenAI 與 Azure OpenAI 模型的統一客戶端。

**OpenAiOfficialEmbeddingModel** - 使用 OpenAI 官方客戶端創建 embeddings（支援 OpenAI 與 Azure OpenAI）。

**ChatModel** - 語言模型的核心介面。

**ChatMemory** - 維持對話歷史。

**ContentRetriever** - 找出 RAG 相關的文件切片。

**DocumentSplitter** - 將文件切割成多個塊。

**EmbeddingModel** - 將文字轉換成數值向量。

**EmbeddingStore** - 儲存與檢索 embeddings。

**MessageWindowChatMemory** - 維持最近訊息的滑動視窗。

**PromptTemplate** - 使用 `{{variable}}` 佔位符打造可重用提示。

**TextSegment** - 含有元資料的文字片段，用於 RAG。

**ToolExecutionRequest** - 代表工具執行請求。

**UserMessage / AiMessage / SystemMessage** - 對話訊息類型。

## AI/ML 概念

**Few-Shot Learning** - 在提示中提供範例。 [模組 02](../02-prompt-engineering/README.md)

**大型語言模型 (LLM)** - 基於巨大文字資料訓練的 AI 模型。

**Reasoning Effort** - GPT-5.2 控制思考深度的參數。 [模組 02](../02-prompt-engineering/README.md)

**Temperature** - 控制輸出隨機性。低＝確定性，高＝創造性。

**Vector Database** - 專用於存取 embeddings 的資料庫。 [模組 03](../03-rag/README.md)

**Zero-Shot Learning** - 無範例完成任務。 [模組 02](../02-prompt-engineering/README.md)

## 防護措施 - [模組 00](../00-quick-start/README.md)

**Defense in Depth** - 多層安全策略，結合應用層防護與供應商安全過濾。

**Hard Block** - 嚴重內容違規時，供應商回傳 HTTP 400 錯誤。

**InputGuardrail** - LangChain4j 介面，用以在 LLM 前驗證使用者輸入，提前阻擋有害提示，節省成本和時延。

**InputGuardrailResult** - 防護措施驗證的回傳型別：`success()` 或 `fatal("reason")`。

**OutputGuardrail** - 在回傳用戶前驗證 AI 回應的介面。

**Provider Safety Filters** - AI 供應商（如 GitHub Models）內建的內容過濾，從 API 層攔截違規。

**Soft Refusal** - 模型禮貌拒絕回答，但不丟出錯誤。

## 提示工程 - [模組 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - 逐步推理以提升準確度。

**Constrained Output** - 強制特定格式或結構。

**High Eagerness** - GPT-5.2 用於深入推理的模式。

**Low Eagerness** - GPT-5.2 用於快速回答的模式。

**Multi-Turn Conversation** - 維持跨輪交流的上下文。

**Role-Based Prompting** - 透過系統訊息設定模型角色。

**Self-Reflection** - 模型自我評價並改進輸出。

**Structured Analysis** - 固定的評估框架。

**Task Execution Pattern** - 計畫 → 執行 → 總結。

## 檢索增強生成 (RAG) - [模組 03](../03-rag/README.md)

**Document Processing Pipeline** - 載入 → 分塊 → 嵌入 → 儲存。

**In-Memory Embedding Store** - 非持久化存儲，供測試使用。

**RAG** - 結合檢索與生成，讓回應有實據。

**Similarity Score** - 語義相似度指標（0-1）。

**Source Reference** - 檢索內容的元資料。

## 代理和工具 - [模組 04](../04-tools/README.md)

**@Tool Annotation** - 標註 Java 方法為 AI 可呼叫工具。

**ReAct Pattern** - 推理 → 行動 → 觀察 → 重複。

**Session Management** - 為不同使用者分隔上下文。

**Tool** - AI 代理可以呼叫的功能。

**Tool Description** - 工具目的和參數說明。

## 代理式模組 - [模組 05](../05-mcp/README.md)

**@Agent Annotation** - 標記介面為 AI 代理，具宣告式行為定義。

**Agent Listener** - 監控代理執行的鉤子，包括 `beforeAgentInvocation()` 和 `afterAgentInvocation()`。

**Agentic Scope** - 代理間共享記憶體，代理使用 `outputKey` 儲存輸出，供後續代理使用。

**AgenticServices** - 透過 `agentBuilder()` 和 `supervisorBuilder()` 創建代理的工廠。

**Conditional Workflow** - 根據條件導向不同專家代理。

**Human-in-the-Loop** - 加入人類審核或內容檢視的工作流程模式。

**langchain4j-agentic** - 用於宣告式代理構建的 Maven 依賴（實驗性）。

**Loop Workflow** - 迭代執行代理直到滿足條件（如品質分數≥0.8）。

**outputKey** - 代理註解參數，指定結果儲存在 Agentic Scope 的位置。

**Parallel Workflow** - 同時執行多個互不相關的代理。

**Response Strategy** - 監督代理決定最終回答的方式：LAST、SUMMARY 或 SCORED。

**Sequential Workflow** - 順序執行代理，輸出為下一步輸入。

**Supervisor Agent Pattern** - 高級代理模式，監督型 LLM 動態決定呼叫哪些子代理。

## 模型上下文協議 (MCP) - [模組 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j 中支援 MCP 的 Maven 依賴。

**MCP** - 模型上下文協議：將 AI 應用連接外部工具的標準，打造一次開發、到處使用。

**MCP Client** - 連接 MCP 伺服器以發現並使用工具的應用。

**MCP Server** - 透過 MCP 提供明確描述和參數 schema 的工具服務。

**McpToolProvider** - LangChain4j 元件，包裝 MCP 工具供 AI 服務與代理使用。

**McpTransport** - MCP 通訊介面，實作包括 Stdio 與 HTTP。

**Stdio Transport** - 透過 stdin/stdout 的本地進程通訊，適合檔案系統存取或命令列工具。

**StdioMcpTransport** - LangChain4j 實作，啟動 MCP 伺服器子程序。

**Tool Discovery** - 客戶端查詢伺服器以獲得工具描述和結構。

## Azure 服務 - [模組 01](../01-introduction/README.md)

**Azure AI Search** - 擁有向量檢索能力的雲端搜尋。 [模組 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - 部署 Azure 資源的工具。

**Azure OpenAI** - 微軟企業級 AI 服務。

**Bicep** - Azure 基礎結構即程式碼語言。 [基礎架構指南](../01-introduction/infra/README.md)

**Deployment Name** - Azure 中模型部署的命名。

**GPT-5.2** - 最新 OpenAI 模型，具備推理控制。 [模組 02](../02-prompt-engineering/README.md)

## 測試與開發 - [測試指南](TESTING.md)

**Dev Container** - 容器化開發環境。 [設定](../../../.devcontainer/devcontainer.json)

**GitHub Models** - 免費的 AI 模型遊樂場。 [模組 00](../00-quick-start/README.md)

**In-Memory Testing** - 使用記憶體儲存進行測試。

**Integration Testing** - 使用真實基礎設施進行測試。

**Maven** - Java 建置自動化工具。

**Mockito** - Java 模擬框架。

**Spring Boot** - Java 應用框架。 [模組 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤譯負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->