# Module 02: 使用 GPT-5.2 的提示工程

## 目錄

- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [了解提示工程](../../../02-prompt-engineering)
- [如何使用 LangChain4j](../../../02-prompt-engineering)
- [核心範例模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索各種範例模式](../../../02-prompt-engineering)
  - [低熱忱 vs 高熱忱](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思程式碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受約束輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [接下來的步驟](../../../02-prompt-engineering)

## 你將學到什麼

在上一個模組中，你已經看過記憶是如何實現會話式 AI，以及如何使用 GitHub Models 進行基本交互。現在我們將專注於你如何提問——也就是提示本身，使用 Azure OpenAI 的 GPT-5.2。你結構化提示的方式會極大影響你獲得回應的品質。

我們會使用 GPT-5.2，因為它引入了推理控制——你可以告訴模型在回答之前進行多少思考。這讓不同的提示策略更為明顯，也幫助你了解何時使用每種方法。相比 GitHub Models，我們還能從 Azure 對 GPT-5.2 較少的速率限制中獲益。

## 先決條件

- 已完成 Module 01（已部署 Azure OpenAI 資源）
- 根目錄下有 `.env` 檔案並含 Azure 憑證（由 Module 01 的 `azd up` 建立）

> **注意：** 如果尚未完成 Module 01，請先依照該模組的部署指示進行。

## 了解提示工程

提示工程是設計輸入文字，使你能持續獲得想要結果的過程。這不只是提問，而是結構化請求，讓模型準確理解你要什麼以及如何呈現。

把它想像成給同事指示。「修正錯誤」太模糊了。「在 UserService.java 第 45 行修正 null pointer exception，加入 null 檢查」就很具體。語言模型也是如此——具體且有結構非常重要。

## 如何使用 LangChain4j

本模組示範了進階的提示範例模式，依然基於之前模組的 LangChain4j，重點在提示結構和推理控制。

<img src="../../../translated_images/zh-MO/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4j 如何將你的提示連接至 Azure OpenAI GPT-5.2*

**相依性** — Module 02 使用 `pom.xml` 中定義的以下 langchain4j 相依套件：
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel 設定** — [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

聊天模型被手動配置為 Spring Bean，使用支援 Azure OpenAI 端點的官方 OpenAI 客戶端。與 Module 01 最大差別在於我們如何結構化發送給 `chatModel.chat()` 的提示，而非模型本身設定。

**系統與使用者訊息** — [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j為清晰起見將訊息類型分離。`SystemMessage` 設定 AI 的行為與上下文（如「你是程式碼審查者」），而 `UserMessage` 則包含實際請求。這讓你能在不同使用者查詢間維持一致的 AI 行為。

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/zh-MO/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage 提供持續上下文，而 UserMessages 包含個別請求*

**多輪對話訊息存取 (MessageWindowChatMemory)** — 多輪對話模式下，我們重用 Module 01 的 `MessageWindowChatMemory`。每個會話都有獨立的記憶實例，存於 `Map<String, ChatMemory>`，讓多個對話可同時進行，不會互相干擾。

**提示範本** — 這裡真正的重點是提示工程，而非新的 LangChain4j API。每種範例模式（低熱忱、高熱忱、任務執行等）都使用相同的 `chatModel.chat(prompt)` 方法，但提示字串經過精心結構化。XML 標籤、指示與格式都是提示文字的一部分，而非 LangChain4j 功能。

**推理控制** — GPT-5.2 的推理程度由提示中的指示控制，如「最多 2 步推理」或「充分探索」。這些是提示工程技術，而非 LangChain4j 配置。此函式庫僅負責將你的提示傳遞給模型。

重點整理：LangChain4j 提供基礎架構（透過 [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java) 連接模型、記憶及訊息處理由 [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 負責），而本模組教你如何在此架構中打造有效的提示。

## 核心範例模式

並非所有問題都適用同一方法，有些提問需要快速回答，有些則需深入思考。有些需可見推理，有些只要結果即可。本模組涵蓋八種提示範例模式，各自優化於不同情境。你將嘗試所有範例，學習何時使用最佳。

<img src="../../../translated_images/zh-MO/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八種提示工程範例及其適用場合概覽*

<img src="../../../translated_images/zh-MO/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*低熱忱（快速直接）與高熱忱（徹底探索）的推理方法比較*

**低熱忱（快速及聚焦）** — 適合簡單問題，希望快速直接回應。模型進行最少推理（最多 2 步）。用於計算、查詢或直接問答。

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **使用 GitHub Copilot 探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 詢問：
> - 「低熱忱與高熱忱的提示模式有何不同？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 的答覆？」
> - 「何時應用自我反思模式，何時用直接指示？」

**高熱忱（深入及徹底）** — 面對複雜問題，希望獲得全面分析。模型徹底探索並呈現詳細推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進度）** — 用於多步驟工作流程。模型先提出整體計劃，執行時逐步敘述，最後做總結。用於遷移、實作或任何多步驟程序。

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

連鎖思考（Chain-of-Thought）提示，明確要求模型展示推理過程，提高複雜任務的準確性。逐步拆解，有助人類與 AI 理解邏輯。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 詢問此模式：
> - 「如何為長時間運行操作調整任務執行模式？」
> - 「在生產環境中，構造工具前言的最佳實務為何？」
> - 「如何捕捉及在使用者介面顯示中間進度更新？」

<img src="../../../translated_images/zh-MO/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*計劃 → 執行 → 總結的多步驟工作流程*

**自我反思程式碼** — 生成生產品質的程式碼。模型先產出程式碼，依品質標準檢查後迭代改進。適用於開發新功能或服務。

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-MO/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*迭代改進循環：生成、評估、識別問題、改進、重複*

**結構化分析** — 提供一致性評估。模型使用固定框架審查程式碼（正確性、實務、效能、安全性）。適用於程式碼審查或品質評估。

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試結構化分析：**
> - 「如何自訂分析框架以適應不同程式碼審查類型？」
> - 「以程式方式解析及處理結構化輸出的最佳方法是什麼？」
> - 「如何確保各審查階段的嚴重程度一致？」

<img src="../../../translated_images/zh-MO/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*以四分類框架搭配嚴重程度，落實一致程式碼審查*

**多輪聊天** — 對話需上下文。模型記得前面訊息並基於此延續。適用於互動協助或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-MO/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*對話上下文於多輪間累積直至達令牌上限*

**逐步推理** — 適用需可見邏輯的問題。模型展示每步推理。用於數學問題、邏輯謎題或想理解思考過程時。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-MO/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*將問題拆解為明確邏輯步驟*

**受約束輸出** — 適合有特定格式要求的回應。模型嚴格遵守格式及長度規則。用於摘要或需要精確輸出結構的場景。

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-MO/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*強制執行特定格式、長度及結構要求*

## 使用現有 Azure 資源

**確認部署：**

確保根目錄下有 `.env` 檔案且含 Azure 憑證（Module 01 部署期間建立）：
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已使用 Module 01 的 `./start-all.sh` 啟動所有應用，本模組已在 8083 埠運行。你可以跳過以下啟動指令，直接前往 http://localhost:8083。

**選項 1：使用 Spring Boot Dashboard （推薦 VS Code 用戶）**

開發容器內包含 Spring Boot Dashboard 擴充套件，提供視覺化介面管理所有 Spring Boot 應用。可在 VS Code 左側活動欄找到（尋找 Spring Boot 圖示）。

透過 Spring Boot Dashboard 你可以：
- 查看工作區內所有可用的 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時查看應用日誌
- 監控應用狀態

只需點擊「prompt-engineering」旁的播放按鈕即可啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用指令行腳本**

啟動所有 Web 應用（模組 01-04）：

**Bash：**
```bash
cd ..  # 從根目錄
./start-all.sh
```

**PowerShell：**
```powershell
cd ..  # 從根目錄
.\start-all.ps1
```

或僅啟動本模組：

**Bash：**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell：**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

兩個腳本均會自動從根目錄 `.env` 載入環境變數，且如果 JAR 不存在會自動編譯。

> **注意：** 若你希望先手動編譯所有模組，再啟動：
>
> **Bash：**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell：**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

於瀏覽器開啟 http://localhost:8083。

**停止應用：**

**Bash：**
```bash
./stop.sh  # 此模組專用
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell：**
```powershell
.\stop.ps1  # 只有此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-MO/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主儀表板顯示所有 8 種提示工程範例及其特性和適用場合*

## 探索各種範例模式

網頁介面讓你嘗試不同提示策略。各模式解決不同問題，試試看才能知道何時最適合用哪種。

### 低熱忱 vs 高熱忱

使用低熱忱模式問個簡單問題，如「200 的 15% 是多少？」你會立即得到直接答案。再用高熱忱問較複雜的題目，如「設計一個高流量 API 的快取策略」。觀察模型如何放慢速度並提供詳細推理。相同模型、相同問題結構，但提示告訴它要思考多少。
<img src="../../../translated_images/zh-MO/low-eagerness-demo.898894591fb23aa0.webp" alt="低積極性示範" width="800"/>

*快速計算與最少推理*

<img src="../../../translated_images/zh-MO/high-eagerness-demo.4ac93e7786c5a376.webp" alt="高積極性示範" width="800"/>

*全面性快取策略（2.8MB）*

### 任務執行（工具序言）

多步驟工作流程受益於事先規劃與過程敘述。模型會概述將做的事情，逐步敘述每一步，然後總結結果。

<img src="../../../translated_images/zh-MO/tool-preambles-demo.3ca4881e417f2e28.webp" alt="任務執行示範" width="800"/>

*逐步敘述建立 REST 端點（3.9MB）*

### 自我反思程式碼

嘗試「建立一個電子郵件驗證服務」。模型不只是產生程式碼然後停止，而是產生後根據品質標準評估、找出弱點並改善。你會看到它迭代直到程式碼達到生產標準。

<img src="../../../translated_images/zh-MO/self-reflecting-code-demo.851ee05c988e743f.webp" alt="自我反思程式碼示範" width="800"/>

*完整電子郵件驗證服務（5.2MB）*

### 結構化分析

程式碼審查需要一致的評估架構。模型使用固定類別（正確性、實踐、效能、安全）和嚴重度等級來分析程式碼。

<img src="../../../translated_images/zh-MO/structured-analysis-demo.9ef892194cd23bc8.webp" alt="結構化分析示範" width="800"/>

*基於框架的程式碼審查*

### 多輪聊天

問「什麼是 Spring Boot？」接著緊接著問「給我一個範例」。模型會記住你的第一個問題，並特別給你一個 Spring Boot 範例。沒有記憶時，第二個問題會太模糊。

<img src="../../../translated_images/zh-MO/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="多輪聊天示範" width="800"/>

*跨問題的上下文保存*

### 逐步推理

挑一個數學問題，分別用逐步推理和低積極性試試看。低積極性只給你答案——快速但不透明。逐步推理會展示每個計算和決策過程。

<img src="../../../translated_images/zh-MO/step-by-step-reasoning-demo.12139513356faecd.webp" alt="逐步推理示範" width="800"/>

*附明確步驟的數學問題*

### 限制輸出

當你需要特定格式或字數時，這種模式能嚴格執行。試試用-點列格式產生正好 100 字的摘要。

<img src="../../../translated_images/zh-MO/constrained-output-demo.567cc45b75da1633.webp" alt="限制輸出示範" width="800"/>

*具格式控制的機器學習摘要*

## 你真正學到的是什麼

**推理努力改變一切**

GPT-5.2 讓你能透過提示控制計算努力。低努力代表快速回應且幾乎不探索。高努力代表模型花時間深入思考。你學會根據任務複雜度調整努力——不要浪費簡單問題的時間，也不要急於複雜決策。

**結構引導行為**

注意提示裡的 XML 標籤？它們不是裝飾。模型比起自由格式文字，更可靠地遵循結構化指令。當你需要多步驟流程或複雜邏輯時，結構有助於模型追蹤進度和下一步。

<img src="../../../translated_images/zh-MO/prompt-structure.a77763d63f4e2f89.webp" alt="提示結構" width="800"/>

*結構良好提示的剖析，帶有明確區段和 XML 樣式組織*

**品質來自自我評估**

自我反思模式透過明確品質標準運作。你不是希望模型「做對」，而是告訴它何謂「做對」：正確邏輯、錯誤處理、效能、安全。模型可以自行評估並改進。這讓程式碼生成從一場抽獎變成一個流程。

**上下文是有限的**

多輪對話靠每次請求帶入訊息歷史。不過有極限——每個模型都有最大 token 數。隨著對話成長，你需要策略保留相關上下文又不超過限制。本模組展示記憶運作原理；後續你會學習何時摘要、何時遺忘、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG （檢索增強生成）](../03-rag/README.md)

---

**導覽：** [← 上一章：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一章：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件是使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保翻譯準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。本公司對使用本翻譯所引致的任何誤解或誤釋不承擔任何責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->