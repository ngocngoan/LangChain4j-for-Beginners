# LangChain4j 詞彙表

## 目錄

- [核心概念](../../../docs)
- [LangChain4j 組件](../../../docs)
- [AI/ML 概念](../../../docs)
- [護欄](../../../docs)
- [提示工程](../../../docs)
- [RAG（檢索增強生成）](../../../docs)
- [代理和工具](../../../docs)
- [代理模組](../../../docs)
- [模型上下文協議 (MCP)](../../../docs)
- [Azure 服務](../../../docs)
- [測試與開發](../../../docs)

快速參考課程中使用的術語和概念。

## 核心概念

**AI 代理** - 使用 AI 進行推理並自主行動的系統。 [模組 04](../04-tools/README.md)

**鏈** - 一系列操作，輸出為下一步輸入。

**分塊** - 將文件分成更小的片段。典型大小：300-500 代幣且有重疊。 [模組 03](../03-rag/README.md)

**上下文視窗** - 模型可處理的最大代幣數。GPT-5.2：400K 代幣。

**嵌入** - 表示文本意義的數值向量。 [模組 03](../03-rag/README.md)

**函數調用** - 模型生成結構化請求以調用外部函數。 [模組 04](../04-tools/README.md)

**幻覺** - 模型生成錯誤但看似合理的信息。

**提示** - 語言模型的文本輸入。 [模組 02](../02-prompt-engineering/README.md)

**語義搜索** - 使用嵌入按意義搜尋，而非關鍵詞。 [模組 03](../03-rag/README.md)

**有狀態 vs 無狀態** - 無狀態：無記憶；有狀態：維護對話歷史。 [模組 01](../01-introduction/README.md)

**代幣** - 模型處理的基本文本單位。影響成本與限制。 [模組 01](../01-introduction/README.md)

**工具鏈** - 按順序執行工具，輸出作為下一次調用依據。 [模組 04](../04-tools/README.md)

## LangChain4j 組件

**AiServices** - 創建類型安全的 AI 服務介面。

**OpenAiOfficialChatModel** - OpenAI 和 Azure OpenAI 模型的統一客戶端。

**OpenAiOfficialEmbeddingModel** - 使用 OpenAI 官方客戶端創建嵌入（支持 OpenAI 與 Azure OpenAI）。

**ChatModel** - 語言模型的核心介面。

**ChatMemory** - 維護對話歷史。

**ContentRetriever** - 為 RAG 尋找相關的文件片段。

**DocumentSplitter** - 將文件分割成片段。

**EmbeddingModel** - 將文本轉換成數值向量。

**EmbeddingStore** - 存儲和檢索嵌入。

**MessageWindowChatMemory** - 維護最近訊息的滑動視窗。

**PromptTemplate** - 創建含有 `{{變數}}` 佔位符的可重用提示。

**TextSegment** - 帶有元數據的文本片段。用於 RAG。

**ToolExecutionRequest** - 代表工具執行請求。

**UserMessage / AiMessage / SystemMessage** - 對話訊息類型。

## AI/ML 概念

**少量示例學習** - 在提示中提供範例。 [模組 02](../02-prompt-engineering/README.md)

**大型語言模型 (LLM)** - 基於大量文本數據訓練的 AI 模型。

**推理努力** - GPT-5.2 參數，控制思考深度。 [模組 02](../02-prompt-engineering/README.md)

**溫度** - 控制輸出隨機度。低=確定性，高=有創意。

**向量資料庫** - 用於嵌入的專用資料庫。 [模組 03](../03-rag/README.md)

**零示例學習** - 無範例執行任務。 [模組 02](../02-prompt-engineering/README.md)

## 護欄 - [模組 00](../00-quick-start/README.md)

**深度防禦** - 多層安全策略，結合應用層護欄和供應商安全過濾。

**強制阻擋** - 嚴重內容違規時，供應商回傳 HTTP 400 錯誤。

**InputGuardrail** - LangChain4j 的介面，用於在輸入到 LLM 前驗證用戶輸入。透過及早封鎖有害提示，節省成本與延遲。

**InputGuardrailResult** - 護欄驗證的返回類型：`success()` 或 `fatal("原因")`。

**OutputGuardrail** - 驗證 AI 回覆合法性的介面，防止不當內容回傳給用戶。

**供應商安全過濾器** - AI 供應商內建的內容過濾器（例如 GitHub 模型），在 API 層面攔截違規。

**軟拒絕** - 模型禮貌地拒絕回答，且不回傳錯誤。

## 提示工程 - [模組 02](../02-prompt-engineering/README.md)

**思維鏈** - 逐步推理以提高準確性。

**限制輸出** - 強制特定格式或結構。

**高積極度** - GPT-5.2 模式，用於徹底推理。

**低積極度** - GPT-5.2 模式，用於快速回答。

**多輪對話** - 跨交流維持上下文。

**基於角色的提示** - 透過系統訊息設定模型身份。

**自我反思** - 模型評估並改進自身輸出。

**結構化分析** - 固定的評估框架。

**任務執行模式** - 計劃 → 執行 → 彙總。

## RAG（檢索增強生成）- [模組 03](../03-rag/README.md)

**文件處理管線** - 載入 → 分塊 → 嵌入 → 儲存。

**記憶體中嵌入庫** - 非持久化儲存，用於測試。

**RAG** - 結合檢索與生成以增強回答基礎。

**相似度分數** - 0 到 1 範圍的語義相似度度量。

**來源參考** - 取回內容的元數據。

## 代理和工具 - [模組 04](../04-tools/README.md)

**@Tool 註解** - 標記 Java 方法為可由 AI 調用的工具。

**ReAct 模式** - 推理 → 行動 → 觀察 → 重複。

**會話管理** - 為不同用戶分離上下文。

**工具** - AI 代理可調用的函數。

**工具說明** - 工具用途與參數文件。

## 代理模組 - [模組 05](../05-mcp/README.md)

**@Agent 註解** - 標記介面為 AI 代理，並以聲明式定義行為。

**代理監聽器** - 透過 `beforeAgentInvocation()` 和 `afterAgentInvocation()` 監控代理執行的鉤子。

**代理範圍** - 代理共享的記憶，用於儲存使用 `outputKey` 的輸出，供下游代理消費。

**AgenticServices** - 工廠，透過 `agentBuilder()` 和 `supervisorBuilder()` 創建代理。

**條件工作流** - 根據條件路由到不同專家代理。

**人機協作** - 在工作流中增加人工檢查點，用於批核或內容審核。

**langchain4j-agentic** - Maven 依賴，支援宣告式代理建構（實驗性）。

**迴圈工作流** - 持續執行代理直到條件達成（例如質量分數 ≥ 0.8）。

**outputKey** - 代理註解參數，指定結果存放在代理範圍中的鍵。

**並行工作流** - 同時運行多個代理，處理獨立任務。

**回應策略** - 主管如何制定最終答案：LAST、SUMMARY 或 SCORED。

**序列工作流** - 按順序執行代理，輸出接續下一步。

**主管代理模式** - 進階代理模式，主管 LLM 動態決定調用哪個子代理。

## 模型上下文協議 (MCP) - [模組 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j 中的 MCP 集成 Maven 依賴。

**MCP** - 模型上下文協議：連接 AI 應用與外部工具的標準。一次構建，到處使用。

**MCP 客戶端** - 連接 MCP 伺服器以發現和使用工具的應用程式。

**MCP 伺服器** - 以 MCP 揭露工具及其描述和參數結構的服務。

**McpToolProvider** - LangChain4j 組件，封裝 MCP 工具供 AI 服務與代理使用。

**McpTransport** - MCP 通訊介面。實作包括 Stdio 和 HTTP。

**Stdio 傳輸** - 透過 stdin/stdout 的本地進程傳輸。適合檔案系統訪問或命令行工具。

**StdioMcpTransport** - LangChain4j 實作，將 MCP 伺服器作為子程序啟動。

**工具發現** - 客戶端查詢伺服器獲取可用工具及其描述和結構。

## Azure 服務 - [模組 01](../01-introduction/README.md)

**Azure AI 搜尋** - 具備向量能力的雲端搜尋。 [模組 03](../03-rag/README.md)

**Azure 開發者命令列工具 (azd)** - 部署 Azure 資源。

**Azure OpenAI** - 微軟企業 AI 服務。

**Bicep** - Azure 基礎架構即程式碼語言。 [基礎架構指南](../01-introduction/infra/README.md)

**部署名稱** - Azure 中模型部署的名稱。

**GPT-5.2** - 最新 OpenAI 模型，帶有推理控制。 [模組 02](../02-prompt-engineering/README.md)

## 測試與開發 - [測試指南](TESTING.md)

**開發容器** - 容器化開發環境。 [配置](../../../.devcontainer/devcontainer.json)

**GitHub 模型** - 免費 AI 模型遊樂場。 [模組 00](../00-quick-start/README.md)

**記憶體中測試** - 使用記憶體儲存進行測試。

**集成測試** - 使用真實基礎架構的測試。

**Maven** - Java 建置自動化工具。

**Mockito** - Java 模擬框架。

**Spring Boot** - Java 應用程式框架。 [模組 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我們致力於確保準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原文文件以其原語言版本應為權威資料。對於重要資訊，建議使用專業人工翻譯。我們不對因使用本翻譯而引起之任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->