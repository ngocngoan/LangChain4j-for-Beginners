# Module 02: 使用 GPT-5.2 的提示工程

## 目錄

- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [如何使用 LangChain4j](../../../02-prompt-engineering)
- [核心模式](../../../02-prompt-engineering)
- [使用現有的 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低與高主動性](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你將學到什麼

在前一個單元中，你已經看到記憶如何支援對話式 AI，並使用 GitHub Models 進行基本互動。現在，我們將專注於你如何提出問題——也就是提示本身——使用 Azure OpenAI 的 GPT-5.2。你組織提示的方式會大幅影響你獲得的回應品質。

我們會使用 GPT-5.2，因為它引入了推理控制功能——你可以告訴模型在回答前要進行多少次思考。這使得不同的提示策略更為明顯，幫助你理解何時使用各種方法。我們還會受益於 Azure 對 GPT-5.2 相較於 GitHub Models 較少的速率限制。

## 先決條件

- 已完成 Module 01（Azure OpenAI 資源部署）
- 根目錄有 `.env` 檔並包含 Azure 憑證（由 Module 01 中的 `azd up` 所建立）

> **注意：** 若尚未完成 Module 01，請先按照那裡的部署指示進行。

## 理解提示工程

提示工程是設計輸入文本，以持續獲得你所需結果的技藝。它不只是提問，還包括結構化請求，讓模型能準確理解你想要什麼以及如何提供。

把它想像成對同事下指令。「修 Bug」太模糊。「修 UserService.java 第 45 行的 NullPointerException，加上 null 檢查」才具體有效。語言模型也是如此——具體與結構化很重要。

## 如何使用 LangChain4j

本單元展示利用前面模組中相同的 LangChain4j 基礎進行進階提示模式，重點在於提示結構與推理控制。

<img src="../../../translated_images/zh-TW/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4j 如何將你的提示連結至 Azure OpenAI GPT-5.2*

**依賴項** - Module 02 使用 `pom.xml` 中定義的以下 langchain4j 依賴：
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

**OpenAiOfficialChatModel 配置** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

聊天模型以 Spring Bean 方式手動配置，使用支援 Azure OpenAI 端點的 OpenAI Official 客戶端。與 Module 01 最大差異在於我們如何結構化傳送至 `chatModel.chat()` 的提示，而非模型本身的設定。

**系統與用戶訊息** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j 明確區分訊息類型以增清晰度。`SystemMessage` 設定 AI 行為與背景（如「你是程式碼審查員」），`UserMessage` 則包含實際的請求。此分離讓你能在不同用戶查詢間維持一致 AI 行為。

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/zh-TW/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage 提供持續背景，UserMessages 含個別請求*

**多輪聊天用 MessageWindowChatMemory** - 多輪會話模式中，我們重用 Module 01 的 `MessageWindowChatMemory`。每場會話有自己的記憶實例，存在 `Map<String, ChatMemory>`，可讓多個並行對話而不致混淆上下文。

**提示模板** - 真正重點是提示工程，而非新的 LangChain4j API。各個模式（低主動性、高主動性、任務執行等）均使用相同的 `chatModel.chat(prompt)` 調用，搭配精心結構化的提示字串。XML 標籤、指示與格式都是提示文字的一部分，不是 LangChain4j 的功能。

**推理控制** - GPT-5.2 的推理力度由提示指令控制，如「最多推理 2 步」或「徹底探索」。這些屬於提示工程技巧，不是 LangChain4j 設定。該函式庫只是將你的提示送至模型。

重點帶走：LangChain4j 提供基礎架構（模型連接透過 [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)、記憶、訊息處理透過 [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)），本模組則教你如何在此架構內打造有效提示。

## 核心模式

並非所有問題都需同一策略。有些題目需快速回答，有些需深入思考。有些需可見推理，有些只需結果。本模組涵蓋八種提示模式——各自優化不同場景。你將嘗試全部，學習何時使用各種方式效果最佳。

<img src="../../../translated_images/zh-TW/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八種提示工程模式及其使用情境概覽*

<img src="../../../translated_images/zh-TW/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*低主動性（快速直接）與高主動性（徹底探索）推理方式比較*

**低主動性（快速且集中）** - 適合簡單問題需即時直達答案。模型推理步驟有限，最多 2 步。適用於計算、查詢或簡單問題。

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **使用 GitHub Copilot 探索：** 打開 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 詢問：
> - 「低主動性和高主動性提示模式有何差異？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「何時使用自我反思模式，何時用直接指令？」

**高主動性（深入且徹底）** - 適用於複雜問題需全面分析。模型會徹底探索並展現詳細推理。適用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** - 適用於多步驟工作流程。模型會先提供計劃，執行時敘述每步，最後做總結。用於遷移、實作或任何多步驟流程。

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

思路鏈（Chain-of-Thought）提示明確要求模型展現推理過程，提升複雜任務的準確度。逐步拆解幫助人類與 AI 了解邏輯。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問此模式：
> - 「如何調整任務執行模式來支援長時間運行操作？」
> - 「生產應用中結構工具前言的最佳實務為何？」
> - 「如何在 UI 中捕捉並顯示中間進度更新？」

<img src="../../../translated_images/zh-TW/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*計劃 → 執行 → 總結的多步任務工作流程*

**自我反思代碼** - 用於產出生產級代碼。模型生成代碼、依據品質標準檢核並迭代改進。用於新功能或服務開發。

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

<img src="../../../translated_images/zh-TW/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*迭代改進循環 - 生成、評估、找問題、改進、重複*

**結構化分析** - 提供一致性評估。模型依固定架構審查代碼（正確性、實務、效能、安全）。用於代碼審查或品質評估。

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

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問結構化分析：
> - 「如何依不同類型代碼審查自訂分析框架？」
> - 「如何程式化解析並根據結構化輸出採取行動？」
> - 「如何確保不同審查會話間嚴重度分類一致？」

<img src="../../../translated_images/zh-TW/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*四類別框架搭配嚴重度分級，確保一致代碼審查*

**多輪對話** - 適用需上下文的聊天。模型記住先前訊息並持續建構。用於互動式協助會話或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-TW/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*對話上下文隨多輪累積直到達標記限制*

**逐步推理** - 適用需顯示邏輯推演的問題。模型明確展現每步推理。用於數學題、邏輯謎題或想要理解思考過程時。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-TW/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*將問題拆解成明確邏輯步驟*

**受限輸出** - 適用需符合特定格式要求的回應。模型嚴格遵守格式與長度規定。用於摘要或需精確輸出結構時。

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

<img src="../../../translated_images/zh-TW/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*強制符合特定格式、長度與結構條件*

## 使用現有的 Azure 資源

**確認部署：**

確認根目錄有 `.env` 檔及 Azure 憑證（在 Module 01 部署期間建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若你已使用 Module 01 的 `./start-all.sh` 啟動所有應用程式，本模組已在 8083 端口運行。你可跳過以下啟動指令，直接訪問 http://localhost:8083 。

**選項 1：使用 Spring Boot 儀表板（推薦 VS Code 使用者）**

開發容器包含 Spring Boot 儀表板外掛，提供可視化介面管理所有 Spring Boot 應用程式。你可於 VS Code 左側活動欄中找到（尋找 Spring Boot 圖示）。

在 Spring Boot 儀表板你可以：
- 查看工作區中所有可用 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 即時查看應用程式日誌
- 監控應用程式狀態

點擊「prompt-engineering」旁的播放按鈕啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有網頁應用程式（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 從根目錄
.\start-all.ps1
```

或只啟動本模組：

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

以上腳本會自動從根目錄 `.env` 載入環境變數，且若 JAR 不存在會自動編譯。

> **注意：** 若你想手動編譯所有模組再啟動：
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

在瀏覽器開啟 http://localhost:8083 。

**停止指令：**

**Bash:**
```bash
./stop.sh  # 僅此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅限此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-TW/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主要儀表板顯示全部 8 種提示工程模式及其特性與使用情境*

## 探索模式

網頁介面允許你嘗試不同的提示策略。每種模式解決不同問題，試用看看何時各方法最合適。

### 低與高主動性

問一個簡單問題，如「200 的 15% 是多少？」使用低主動性。你會得到即時且直接的答案。接著用高主動性問一個複雜問題，如「設計一個高流量 API 的快取策略」。觀察模型如何放慢速度，提供詳盡推理。相同模型、相同問題架構，但提示告訴它該思考多少。
<img src="../../../translated_images/zh-TW/low-eagerness-demo.898894591fb23aa0.webp" alt="低熱忱示範" width="800"/>

*快速計算，推理極少*

<img src="../../../translated_images/zh-TW/high-eagerness-demo.4ac93e7786c5a376.webp" alt="高熱忱示範" width="800"/>

*全面快取策略（2.8MB）*

### 任務執行（工具前置提示）

多步驟工作流程受益於事先規劃和進度說明。模型會描述它將執行的動作，敘述每個步驟，然後總結結果。

<img src="../../../translated_images/zh-TW/tool-preambles-demo.3ca4881e417f2e28.webp" alt="任務執行示範" width="800"/>

*創建帶有逐步說明的 REST 端點（3.9MB）*

### 自我反思程式碼

嘗試「建立一個電子郵件驗證服務」。模型不只生成程式碼然後停止，而是生成後依據品質標準進行評估，找出缺點並改進。你會看到它反覆迭代，直到程式碼達到生產標準。

<img src="../../../translated_images/zh-TW/self-reflecting-code-demo.851ee05c988e743f.webp" alt="自我反思程式碼示範" width="800"/>

*完整的電子郵件驗證服務（5.2MB）*

### 結構化分析

程式碼審查需要一致的評估架構。模型使用固定分類（正確性、實務、效能、安全性）及嚴重性層級來分析程式碼。

<img src="../../../translated_images/zh-TW/structured-analysis-demo.9ef892194cd23bc8.webp" alt="結構化分析示範" width="800"/>

*基於框架的程式碼審查*

### 多輪對話

問「什麼是 Spring Boot？」然後緊接著問「給我一個範例」。模型會記得第一個問題，並專門給你一個 Spring Boot 範例。若無記憶，第二個問題太過模糊。

<img src="../../../translated_images/zh-TW/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="多輪對話示範" width="800"/>

*跨問題保持上下文*

### 逐步推理

選一個數學問題，用「逐步推理」和「低熱忱」兩種方式試試。低熱忱直接給答案──快速但難懂。逐步推理則展示所有計算與決策。

<img src="../../../translated_images/zh-TW/step-by-step-reasoning-demo.12139513356faecd.webp" alt="逐步推理示範" width="800"/>

*帶有明確步驟的數學問題*

### 限制輸出

當你需要特定格式或字數時，這個模式能嚴格遵守。嘗試產生一個剛好 100 字、以項目符號呈現的摘要。

<img src="../../../translated_images/zh-TW/constrained-output-demo.567cc45b75da1633.webp" alt="限制輸出示範" width="800"/>

*具有格式控制的機器學習摘要*

## 你真正學到的是

**推理努力改變一切**

GPT-5.2 讓你透過提示控制計算努力程度。低努力意味著快速回應並極少探索。高努力代表模型花時間深入思考。你正在學會依任務複雜度調整努力──別浪費時間在簡單問題，但複雜決策也別急著下結論。

**結構引導行為**

有注意到提示中的 XML 標籤嗎？它們不是裝飾。模型依循結構化指令比自由文本更可靠。當你需要多步驟流程或複雜邏輯時，結構幫助模型追蹤目前位置和下一步。

<img src="../../../translated_images/zh-TW/prompt-structure.a77763d63f4e2f89.webp" alt="提示結構" width="800"/>

*架構清晰且層次分明的提示結構*

**品質來自自我評估**

自我反思模式透過明確制定品質標準運作。不再只希望模型「做對」，而是明確告訴它「對」的定義：正確邏輯、錯誤處理、效能、安全。模型因此可以自我評估輸出並持續改進，將程式碼生成從抽獎變成流程。

**上下文是有限的**

多輪對話是靠每次請求包含訊息歷史達成。但有上限──每個模型都有最大 Token 限制。隨著對話增長，你需要策略來保留相關上下文而不超限。此模組說明記憶運作，未來你將學會何時摘要、何時遺忘、何時提取。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導覽：** [← 上一節：模組 01 - 介紹](../01-introduction/README.md) | [回到主頁](../README.md) | [下一節：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤譯負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->