# LangChain4j 詞彙表

## 目錄

- [核心概念](../../../docs)
- [LangChain4j 組件](../../../docs)
- [AI/機器學習概念](../../../docs)
- [守護措施](../../../docs)
- [提示工程](../../../docs)
- [RAG（檢索增強生成）](../../../docs)
- [代理和工具](../../../docs)
- [代理模組](../../../docs)
- [模型上下文協議（MCP）](../../../docs)
- [Azure 服務](../../../docs)
- [測試與開發](../../../docs)

課程中使用術語與概念的快速參考。

## 核心概念

**AI 代理** - 使用 AI 進行推理並自主行動的系統。[模組 04](../04-tools/README.md)

**Chain** - 一連串將輸出作為下一步輸入的操作序列。

**分塊** - 將文件拆分成較小部分。典型：300-500 個標記並含重疊。[模組 03](../03-rag/README.md)

**上下文視窗** - 模型可處理的最大標記數。GPT-5.2：400K 個標記。

**嵌入向量** - 代表文本意義的數字向量。[模組 03](../03-rag/README.md)

**函數調用** - 模型生成結構化請求以調用外部函數。[模組 04](../04-tools/README.md)

**幻覺** - 模型產生錯誤但看似可信的信息。

**提示** - 輸入給語言模型的文本。[模組 02](../02-prompt-engineering/README.md)

**語義搜尋** - 通過嵌入向量進行語義搜尋，而非關鍵詞搜索。[模組 03](../03-rag/README.md)

**有狀態 vs 無狀態** - 無狀態：無記憶。有狀態：維持對話歷史。[模組 01](../01-introduction/README.md)

**標記** - 模型處理的基本文本單位。影響成本與限制。[模組 01](../01-introduction/README.md)

**連接工具鏈** - 工具按序執行，輸出為下一次調用的輸入。[模組 04](../04-tools/README.md)

## LangChain4j 組件

**AiServices** - 創建型別安全的 AI 服務介面。

**OpenAiOfficialChatModel** - OpenAI 與 Azure OpenAI 模型的統一客戶端。

**OpenAiOfficialEmbeddingModel** - 使用 OpenAI 官方客戶端創建嵌入向量（支援 OpenAI 與 Azure OpenAI）。

**ChatModel** - 語言模型的核心介面。

**ChatMemory** - 維護對話歷史。

**ContentRetriever** - 查找 RAG 使用的相關文件分塊。

**DocumentSplitter** - 將文件拆分為塊。

**EmbeddingModel** - 將文本轉換為數字向量。

**EmbeddingStore** - 存儲與檢索嵌入向量。

**MessageWindowChatMemory** - 維護最近消息的滑動視窗。

**PromptTemplate** - 使用 `{{variable}}` 佔位符建立可重用提示。

**TextSegment** - 帶有元數據的文本片段。用於 RAG。

**ToolExecutionRequest** - 表示工具執行請求。

**UserMessage / AiMessage / SystemMessage** - 對話消息類型。

## AI/機器學習概念

**少量學習** - 在提示中提供範例。[模組 02](../02-prompt-engineering/README.md)

**大型語言模型（LLM）** - 使用龐大文本資料訓練的 AI 模型。

**推理力度** - GPT-5.2 參數，控制思考深度。[模組 02](../02-prompt-engineering/README.md)

**溫度** - 控制輸出隨機性。低=確定性，高=創造性。

**向量資料庫** - 用於嵌入向量的專用資料庫。[模組 03](../03-rag/README.md)

**零樣本學習** - 無需範例即可執行任務。[模組 02](../02-prompt-engineering/README.md)

## 守護措施 - [模組 00](../00-quick-start/README.md)

**深度防禦** - 多層安全策略，結合應用層守護與供應商安全過濾。

**硬封鎖** - 供應商對嚴重違規內容回傳 HTTP 400 錯誤。

**InputGuardrail** - LangChain4j 介面，於用戶輸入到達 LLM 前驗證，及早阻擋有害提示，節省成本與延遲。

**InputGuardrailResult** - 守護驗證返回類型：`success()` 或 `fatal("原因")`。

**OutputGuardrail** - AI 回應交付用戶前的驗證介面。

**供應商安全過濾器** - 由 AI 供應商（如 GitHub 模型）提供的 API 級內建內容過濾。

**軟拒絕** - 模型禮貌拒絕回答，且不拋錯。

## 提示工程 - [模組 02](../02-prompt-engineering/README.md)

**連鎖思維** - 逐步推理以提升準確度。

**約束輸出** - 強制定義特定格式或結構。

**高度熱忱** - GPT-5.2 模式，強化深入推理。

**低度熱忱** - GPT-5.2 模式，快速輸出答案。

**多輪對話** - 維持上下文連貫性。

**基於角色的提示** - 透過系統消息設置模型角色。

**自我反思** - 模型評估並改進自身輸出。

**結構化分析** - 固定的評估框架。

**任務執行模式** - 計劃 → 執行 → 總結。

## RAG（檢索增強生成） - [模組 03](../03-rag/README.md)

**文件處理流程** - 載入 → 分塊 → 嵌入 → 儲存。

**記憶體內嵌入儲存** - 非持久存儲，用於測試。

**RAG** - 結合檢索與生成來根植回應。

**相似度評分** - 語義相似度衡量 (0-1)。

**來源引用** - 檢索內容的元數據。

## 代理和工具 - [模組 04](../04-tools/README.md)

**@Tool 標註** - 標示 Java 方法為可由 AI 調用的工具。

**ReAct 模式** - 推理 → 行動 → 觀察 → 重複。

**會話管理** - 不同用戶分離上下文。

**工具** - AI 代理可調用的函數。

**工具描述** - 工具目的與參數文件。

## 代理模組 - [模組 05](../05-mcp/README.md)

**@Agent 標註** - 將介面標註為 AI 代理，聲明行為定義。

**代理監聽器** - 可透過 `beforeAgentInvocation()` 和 `afterAgentInvocation()` 監控代理執行。

**代理範圍** - 代理使用 `outputKey` 儲存輸出結果供後續代理消費的共享記憶體。

**AgenticServices** - 使用 `agentBuilder()` 及 `supervisorBuilder()` 創建代理的工廠。

**條件工作流** - 根據條件路由至不同專家代理。

**人機介入工作流** - 添加人工介面點以供批准或內容審核的流程。

**langchain4j-agentic** - 宣告型代理構建的 Maven 依賴（實驗性）。

**循環工作流** - 重複執行代理直到條件達成（如質量分數 ≥ 0.8）。

**outputKey** - 代理標註參數，指定結果存放於代理範圍的位置。

**並行工作流** - 同時運行多個代理完成獨立任務。

**回應策略** - 主管如何形成最終答案：LAST、SUMMARY 或 SCORED。

**序列工作流** - 按順序執行代理，輸出流向下一步。

**主管代理模式** - 進階代理模式，由主管 LLM 動態決定調用哪些子代理。

## 模型上下文協議（MCP） - [模組 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j 對 MCP 整合的 Maven 依賴。

**MCP** - 模型上下文協議：連接 AI 應用與外部工具的標準。一次建立，到處使用。

**MCP 客戶端** - 連接 MCP 伺服器以發現和使用工具的應用。

**MCP 伺服器** - 以 MCP 形式暴露工具，含清晰描述與參數結構的服務。

**McpToolProvider** - LangChain4j 組件，將 MCP 工具包裝為 AI 服務與代理可用。

**McpTransport** - MCP 通信介面。實現包括 Stdio 與 HTTP。

**Stdio 傳輸** - 通過 stdin/stdout 的本地進程傳輸。適用於檔案系統存取或命令列工具。

**StdioMcpTransport** - LangChain4j 實現，作為子程序啟動 MCP 伺服器。

**工具發現** - 客戶端查詢伺服器取得可用工具及其描述與結構。

## Azure 服務 - [模組 01](../01-introduction/README.md)

**Azure AI 搜尋** - 具向量能力的雲端搜尋。[模組 03](../03-rag/README.md)

**Azure 開發者 CLI (azd)** - 部署 Azure 資源。

**Azure OpenAI** - 微軟企業 AI 服務。

**Bicep** - Azure 基礎架構即程式語言。[基礎架構指南](../01-introduction/infra/README.md)

**部署名稱** - Azure 中模型部署的名稱。

**GPT-5.2** - 最新 OpenAI 模型，具推理控制能力。[模組 02](../02-prompt-engineering/README.md)

## 測試與開發 - [測試指南](TESTING.md)

**開發容器** - 容器化開發環境。[設定](../../../.devcontainer/devcontainer.json)

**GitHub 模型** - 免費 AI 模型實驗場。[模組 00](../00-quick-start/README.md)

**記憶體內測試** - 使用記憶體儲存的測試。

**整合測試** - 使用實際基礎設施的測試。

**Maven** - Java 構建自動化工具。

**Mockito** - Java 模擬框架。

**Spring Boot** - Java 應用框架。[模組 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件是使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要信息，建議採用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->