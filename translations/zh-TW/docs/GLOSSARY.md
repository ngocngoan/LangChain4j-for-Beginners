# LangChain4j 術語表

## 目錄

- [核心概念](../../../docs)
- [LangChain4j 元件](../../../docs)
- [AI/ML 概念](../../../docs)
- [守護措施](../../../docs)
- [提示工程](../../../docs)
- [RAG（檢索增強生成）](../../../docs)
- [代理程式與工具](../../../docs)
- [Agentic 模組](../../../docs)
- [模型上下文協定（MCP）](../../../docs)
- [Azure 服務](../../../docs)
- [測試與開發](../../../docs)

本課程所用術語與概念的快速參考。

## 核心概念

**AI 代理程式** - 使用 AI 自主推理與行動的系統。 [模組 04](../04-tools/README.md)

**鏈（Chain）** - 依序執行操作，輸出作為下一步輸入的序列。

**分塊（Chunking）** - 將文件拆分成較小部分。典型大小：300-500 個標記，有重疊。 [模組 03](../03-rag/README.md)

**上下文視窗** - 模型可處理的最大標記數。GPT-5.2：400K 標記（最多 272K 輸入，128K 輸出）。

**嵌入（Embeddings）** - 表示文字意義的數值向量。 [模組 03](../03-rag/README.md)

**函數呼叫（Function Calling）** - 模型生成結構化請求以呼叫外部函數。 [模組 04](../04-tools/README.md)

**幻覺（Hallucination）** - 模型生成錯誤但看似合理的資訊。

**提示（Prompt）** - 輸入給語言模型的文字。 [模組 02](../02-prompt-engineering/README.md)

**語意搜尋（Semantic Search）** - 透過意義使用嵌入搜尋，不是關鍵字。 [模組 03](../03-rag/README.md)

**有狀態 vs 無狀態** - 無狀態：無記憶。有狀態：保留對話歷史。 [模組 01](../01-introduction/README.md)

**標記（Tokens）** - 模型處理的基本文字單位。影響成本和限制。 [模組 01](../01-introduction/README.md)

**工具串接（Tool Chaining）** - 依序執行工具，輸出作為下一次呼叫的資訊。 [模組 04](../04-tools/README.md)

## LangChain4j 元件

**AiServices** - 建立型別安全的 AI 服務介面。

**OpenAiOfficialChatModel** - OpenAI 和 Azure OpenAI 模型的統一用戶端。

**OpenAiOfficialEmbeddingModel** - 使用 OpenAI 官方用戶端建立嵌入（支援 OpenAI 與 Azure OpenAI）。

**ChatModel** - 語言模型核心介面。

**ChatMemory** - 維護對話歷史。

**ContentRetriever** - 尋找 RAG 用的相關文件片段。

**DocumentSplitter** - 將文件拆成多個片段。

**EmbeddingModel** - 將文字轉換成數值向量。

**EmbeddingStore** - 儲存與檢索嵌入。

**MessageWindowChatMemory** - 維持最近訊息的滑動視窗。

**PromptTemplate** - 使用 `{{variable}}` 變數建立可重用提示。

**TextSegment** - 附帶元資料的文字片段，用於 RAG。

**ToolExecutionRequest** - 表示工具執行請求。

**UserMessage / AiMessage / SystemMessage** - 對話的訊息類型。

## AI/ML 概念

**少量示例學習（Few-Shot Learning）** - 在提示中提供示範範例。 [模組 02](../02-prompt-engineering/README.md)

**大型語言模型（LLM）** - 在龐大文字資料上訓練的 AI 模型。

**推理深度（Reasoning Effort）** - GPT-5.2 參數，用於控制思考深度。 [模組 02](../02-prompt-engineering/README.md)

**溫度（Temperature）** - 控制輸出隨機性。低值＝決定性，高值＝有創意。

**向量資料庫（Vector Database）** - 專門儲存嵌入的資料庫。 [模組 03](../03-rag/README.md)

**零示例學習（Zero-Shot Learning）** - 無範例直接執行任務。 [模組 02](../02-prompt-engineering/README.md)

## 守護措施 - [模組 00](../00-quick-start/README.md)

**多層防禦（Defense in Depth）** - 多層安控策略，結合應用層守護與供應商安全篩選。

**硬封鎖（Hard Block）** - 嚴重內容違規導致供應商返回 HTTP 400 錯誤。

**InputGuardrail** - LangChain4j 介面，驗證使用者輸入避免送至 LLM，節省成本與延遲並阻擋有害提示。

**InputGuardrailResult** - 守護結果回傳類型：`success()` 或 `fatal("reason")`。

**OutputGuardrail** - 驗證 AI 回應內容，確保安全再回傳使用者。

**供應商安全篩選（Provider Safety Filters）** - AI 供應商內建內容篩選（如 GitHub Models），API 階段攔截違規。

**軟拒絕（Soft Refusal）** - 模型禮貌拒絕回答但不丟錯誤。

## 提示工程 - [模組 02](../02-prompt-engineering/README.md)

**思維鏈（Chain-of-Thought）** - 逐步推理提高準確度。

**受限輸出（Constrained Output）** - 強制特定格式或結構。

**高推理模式（High Eagerness）** - GPT-5.2 的詳盡推理模式。

**低推理模式（Low Eagerness）** - GPT-5.2 的快速回答模式。

**多輪對話（Multi-Turn Conversation）** - 保持跨回合上下文。

**角色指定提示（Role-Based Prompting）** - 透過系統訊息設定模型身份。

**自我反省（Self-Reflection）** - 模型自行評估並改進輸出。

**結構化分析（Structured Analysis）** - 固定評估框架。

**任務執行模式（Task Execution Pattern）** - 計畫 → 執行 → 彙總。

## RAG（檢索增強生成）- [模組 03](../03-rag/README.md)

**文件處理流程（Document Processing Pipeline）** - 載入 → 分塊 → 嵌入 → 儲存。

**記憶體中嵌入存儲（In-Memory Embedding Store）** - 測試用非持久化存儲。

**RAG** - 結合檢索與生成以確保回應有根據。

**相似度分數（Similarity Score）** - 語意相似度評分（0-1）。

**來源引用（Source Reference）** - 檢索內容的元資料。

## 代理程式與工具 - [模組 04](../04-tools/README.md)

**@Tool 註解** - 標記 Java 方法為 AI 可呼叫工具。

**ReAct 模式** - 推理 → 行動 → 觀察 → 重複。

**會話管理（Session Management）** - 為不同用戶分開上下文。

**工具（Tool）** - AI 代理程式可呼叫的函數。

**工具說明（Tool Description）** - 工具目的與參數文件。

## Agentic 模組 - [模組 05](../05-mcp/README.md)

**@Agent 註解** - 標記介面為 AI 代理程式並以聲明方式定義行為。

**Agent 監聽器（Agent Listener）** - 透過 `beforeAgentInvocation()` 和 `afterAgentInvocation()` 監控代理執行。

**Agentic 範圍** - 代理共享記憶，使用 `outputKey` 存放輸出以供下游代理消費。

**AgenticServices** - 使用 `agentBuilder()` 和 `supervisorBuilder()` 建立代理工廠。

**條件化工作流（Conditional Workflow）** - 根據條件路由至不同專家代理。

**人類監督（Human-in-the-Loop）** - 增設人為審查或批准工作流模式。

**langchain4j-agentic** - 用於聲明式代理建立的 Maven 依賴（實驗性）。

**迴圈工作流（Loop Workflow）** - 重複執行代理直到條件達成（例如品質分數 ≥ 0.8）。

**outputKey** - 代理註解參數，指定結果存放於 Agentic 範圍的位置。

**平行工作流（Parallel Workflow）** - 同時執行多個獨立代理任務。

**回應策略（Response Strategy）** - 主管代理形成最終答案的方式：LAST（最後一次）、SUMMARY（彙整）、SCORED（打分）。

**序列工作流（Sequential Workflow）** - 按順序執行代理，輸出作為下一步輸入。

**主管代理模式（Supervisor Agent Pattern）** - 高階代理模式，主管 LLM 動態決定呼叫哪些子代理。

## 模型上下文協定（MCP）- [模組 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j 的 MCP 整合 Maven 依賴。

**MCP** - 模型上下文協定：連結 AI 應用與外部工具的標準。一次建置，到處可用。

**MCP 用戶端** - 連接 MCP 服務器以發現與使用工具的應用程式。

**MCP 服務器** - 透過 MCP 對外曝工具服務，包含清楚說明與參數規範。

**McpToolProvider** - LangChain4j 元件，封裝 MCP 工具供 AI 服務與代理使用。

**McpTransport** - MCP 通訊介面。實作包含 Stdio 與 HTTP。

**Stdio 傳輸** - 透過 stdin/stdout 的本地程序傳輸。適用於檔案系統訪問或命令列工具。

**StdioMcpTransport** - LangChain4j 實作，以子流程啟動 MCP 服務器。

**工具發現（Tool Discovery）** - 用戶端查詢服務器以取得可用工具及說明和參數結構。

## Azure 服務 - [模組 01](../01-introduction/README.md)

**Azure AI 搜尋** - 具向量功能的雲端搜尋服務。 [模組 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - 部署 Azure 資源。

**Azure OpenAI** - 微軟的企業級 AI 服務。

**Bicep** - Azure 基礎設施即程式碼語言。 [基礎設施指南](../01-introduction/infra/README.md)

**部署名稱** - Azure 中模型部署的名稱。

**GPT-5.2** - 具備推理控制的最新 OpenAI 模型。 [模組 02](../02-prompt-engineering/README.md)

## 測試與開發 - [測試指南](TESTING.md)

**開發容器（Dev Container）** - 容器化開發環境。 [設定](../../../.devcontainer/devcontainer.json)

**GitHub 模型** - 免費 AI 模型試用平台。 [模組 00](../00-quick-start/README.md)

**記憶體中測試（In-Memory Testing）** - 使用記憶體存儲執行測試。

**整合測試（Integration Testing）** - 使用真實基礎設施執行測試。

**Maven** - Java 建置自動化工具。

**Mockito** - Java 模擬框架。

**Spring Boot** - Java 應用程式框架。 [模組 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不精確之處。原始文件之母語版本應視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯內容所產生之任何誤解或誤譯負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->