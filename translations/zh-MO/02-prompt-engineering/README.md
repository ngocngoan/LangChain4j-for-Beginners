# Module 02: 使用 GPT-5.2 進行提示工程

## 目錄

- [影片導覽](../../../02-prompt-engineering)
- [你將學到的內容](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零樣本提示](../../../02-prompt-engineering)
  - [少樣本提示](../../../02-prompt-engineering)
  - [思路鏈](../../../02-prompt-engineering)
  - [角色基礎提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有的 Azure 資源](../../../02-prompt-engineering)
- [應用截圖](../../../02-prompt-engineering)
- [探索這些模式](../../../02-prompt-engineering)
  - [低與高急切度](../../../02-prompt-engineering)
  - [任務執行（工具序言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [後續步驟](../../../02-prompt-engineering)

## 影片導覽

觀看此現場示範說明如何開始本模組：[Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## 你將學到的內容

<img src="../../../translated_images/zh-MO/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

在前一模組中，你已了解記憶如何推動對話式 AI 並使用 GitHub 模型進行基本互動。現在我們將專注於如何提問——也就是提示本身——使用 Azure OpenAI 的 GPT-5.2。你結構提示的方式會大幅影響回應品質。我們先回顧基本提示技術，接着深入介紹八種進階模式，充分發揮 GPT-5.2 的能力。

我們使用 GPT-5.2 是因為它引入了推理控制——你能指示模型在回答前要思考多少。這讓不同提示策略更明顯，也幫助你理解何時使用哪種方法。此外，GPT-5.2 相較於 GitHub 模型在 Azure 上的速率限制更少，對開發更有利。

## 先決條件

- 完成模組 01（已部署 Azure OpenAI 資源）
- 根目錄下有包含 Azure 認證的 `.env` 檔案（由模組 01 的 `azd up` 建立）

> **注意：** 若尚未完成模組 01，請先依據該模組的部署說明操作。

## 理解提示工程

<img src="../../../translated_images/zh-MO/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

提示工程是設計輸入文字，使你穩定獲得所需結果。這不只是問問題，而是組織請求，讓模型完全理解你想要什麼，以及如何輸出。

把它想成給同事下指令。「修復錯誤」很模糊。「在 UserService.java 第 45 行加空值檢查以修復 Null Pointer Exception」很具體。語言模型也是如此——具體且結構化很重要。

<img src="../../../translated_images/zh-MO/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j 提供基礎設施——模型連接、記憶體與訊息類型——而提示模式則是你透過該基礎設施發送的結構化文本。關鍵組成是 `SystemMessage`（設定 AI 行為與角色）與 `UserMessage`（攜帶你實際的請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-MO/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

在深入本模組的進階模式前，我們先複習五種基本提示技巧。這是每位提示工程師應知的基石。如果你已讀過[快速上手模組](../00-quick-start/README.md#2-prompt-patterns)，你已見過這些的實例——以下是它們背後的概念框架。

### 零樣本提示

最簡單的方式：直接指令模型，無範例。模型完全依賴訓練理解並執行任務。適合行為明確且單純的請求。

<img src="../../../translated_images/zh-MO/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*無範例的直接指令 — 模型單從指令推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應：「正面」
```

**適用時機：** 簡單分類、直接問題、翻譯或任何模型能不靠額外指導處理的任務。

### 少樣本提示

提供示範範例，展現你希望模型遵循的模式。模型學習示例中的輸入輸出格式，並運用於新輸入。這大幅提升所需格式不明顯的任務一致性。

<img src="../../../translated_images/zh-MO/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*透過範例學習—模型辨識模式並應用於新輸入*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**適用時機：** 客製分類、一致格式、特定領域任務，或零樣本結果不穩定時。

### 思路鏈

要求模型逐步展示推理過程。模型非直接跳答案，而是拆解問題，明確列出每個環節。提升數學、邏輯與多步推理任務的準確度。

<img src="../../../translated_images/zh-MO/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*逐步推理 — 將複雜問題拆成明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**適用時機：** 數學題、邏輯謎題、除錯或任何展示推理流程能提升準確度與信任的任務。

### 角色基礎提示

在提問前為 AI 設定角色。此舉提供語境，影響回答的語氣、深度與重點。比方說「軟體架構師」會給不同建議，與「初級開發者」或「安全稽核員」不一樣。

<img src="../../../translated_images/zh-MO/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*設置背景與角色 — 同一問題依指派角色會有不同回應*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**適用時機：** 程式碼審查、教學、特定領域分析，或需針對專業層級調整回應時。

### 提示模板

創建可重複使用的提示，內含變數佔位符。不必每次寫新提示，可定義一次模板並填入不同變數。LangChain4j 的 `PromptTemplate` 類用 `{{variable}}` 語法便捷實現。

<img src="../../../translated_images/zh-MO/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*含變數佔位符的重用提示 — 一個模板，多種用法*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**適用時機：** 多次查詢不同輸入、批次處理、建立可重用 AI 工作流程，或需保持提示結構不變但數據變化時。

---

這五項基礎為大多數提示任務提供堅實工具組。本模組接續這些，以 **八種進階模式** 構築，利用 GPT-5.2 的推理控制、自我評估與結構化輸出功能。

## 進階模式

掌握基礎後，來看本模組獨有的八種進階模式。不是每個問題都用同一招。有些提問需要快答，有些需深入思考。有些需顯示推理，有些只需結果。以下每種模式針對不同場景優化——GPT-5.2 的推理控制讓差異更加明顯。

<img src="../../../translated_images/zh-MO/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八種提示工程模式及其適用範疇概覽*

<img src="../../../translated_images/zh-MO/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 的推理控制讓你指定模型思考量——從快速直接答覆到深度探索*

**低急切度（快速且聚焦）** — 適用於需要快速、直接回應的簡單問題。模型推理最少，最多 2 步。適合計算、查詢或單純問題。

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **利用 GitHub Copilot 探索：** 打開 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 並提問：
> - 「低急切度與高急切度提示模式差異是什麼？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「何時使用自我反思模式、何時用直接指令？」

**高急切度（深入且全面）** — 適合複雜問題需要完整分析。模型深入探索並呈現細緻推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進行）** — 多步流程模式。模型先提出計劃，邊執行邊解說各步，最後匯總。適用搬遷、實作或任何多步程序。

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

思路鏈提示明確要求模型展示推理過程，提升複雜任務準確度。逐步拆解幫助人與 AI 理解邏輯。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 詢問此模式：
> - 「如何適配長時運作的任務執行模式？」
> - 「生產環境中組織工具序言的最佳實踐是？」
> - 「如何在 UI 中抓取並顯示中間進度更新？」

<img src="../../../translated_images/zh-MO/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*計劃 → 執行 → 總結的多步任務流程*

**自我反思代碼** — 產出高質量生產代碼。模型遵循生產標準產生代碼，並實作適當錯誤處理。用於功能或服務開發。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-MO/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*迭代改善循環——產生、評估、找問題、改進、重複*

**結構化分析** — 用於一致性評估。模型依固定框架審查程式碼（正確性、實踐、性能、安全、維護性）。適合程式碼審查或品質評估。

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 詢問結構化分析：
> - 「如何客製不同程式碼審查類型的分析框架？」
> - 「以程式方式解析並利用結構化輸出最佳方式是？」
> - 「如何確保不同審查間的一致嚴重度評定？」

<img src="../../../translated_images/zh-MO/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*帶嚴重度分級的一致程式碼審查框架*

**多輪對話** — 需要上下文的對話。模型記憶前文並累積資訊。適用互動輔助或複雜問答。

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

*對話上下文隨多輪累積直至達標記限制*

**逐步推理** — 要求展示明確邏輯。模型清楚說明每步推理。用於數學題、邏輯謎題或需理解思考過程者。

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

*將問題拆解成明確邏輯步驟*

**受限輸出** — 回應有特定格式需求。模型嚴格遵守格式與長度規則。用於摘要或需精準輸出結構時。

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

*強制特定格式、長度與結構要求*

## 使用現有的 Azure 資源

**驗證部署：**

確保根目錄有 `.env` 檔案包含 Azure 認證（模組 01 過程中建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用：**

> **注意：** 若你已在模組 01 使用 `./start-all.sh` 啟動所有應用，本模組已在 8083 埠運行。可略過以下啟動指令，直接訪問 http://localhost:8083。

**選項 1：使用 Spring Boot 控制面板（推薦 VS Code 使用者）**
開發容器包含了 Spring Boot Dashboard 擴充功能，提供了可視化介面來管理所有 Spring Boot 應用程式。你可以在 VS Code 左側的活動欄找到它（尋找 Spring Boot 圖示）。

從 Spring Boot Dashboard 你可以：
- 查看工作區中所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 實時查看應用程式日誌
- 監控應用程式狀態

只需點擊「prompt-engineering」旁的播放按鈕即可啟動此模組，或同時啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有網頁應用程式（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄開始
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 從根目錄開始
.\start-all.ps1
```

或只啟動此模組：

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

這兩個腳本會自動從根目錄的 `.env` 檔案載入環境變數，並且如果 JAR 檔不存在，會進行編譯。

> **注意：** 如果你想在啟動前手動編譯所有模組：
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

在瀏覽器打開 http://localhost:8083 。

**停止應用程式：**

**Bash:**
```bash
./stop.sh  # 只有此模組
# 或者
cd .. && ./stop-all.sh  # 全部模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅限此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-MO/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主面板顯示所有 8 種 prompt engineering 範例及其特點和使用案例*

## 探索各種範例

網頁介面讓你試驗不同的提示策略。每種範例解決不同的問題——試試看，看看哪種方法效果最好。

> **注意：串流與非串流** — 每個範例頁面都有兩個按鈕：**🔴 串流回應（即時）** 和 **非串流** 選項。串流使用 Server-Sent Events (SSE) 即時顯示模型生成的 token，所以你可以立刻看到進度。非串流選項則會等整個回應結束後才顯示。對於觸發深度推理的提示（例如，高積極性、自我反思程式碼），非串流回應可能非常耗時——有時甚至需要數分鐘，且沒有任何回饋。**在嘗試複雜提示時，建議使用串流**，這樣可以看到模型正努力運作，避免誤以為請求超時。
>
> **注意：瀏覽器要求** — 串流功能使用 Fetch Streams API（`response.body.getReader()`），需要完整的瀏覽器支援（Chrome、Edge、Firefox、Safari）。在 VS Code 內建的簡易瀏覽器中無法運作，因為其網頁視圖不支援 ReadableStream API。如果使用簡易瀏覽器，非串流按鈕仍能正常工作，只有串流按鈕會失效。建議在外部瀏覽器打開 `http://localhost:8083` 以獲得完整體驗。

### 低積極性 vs 高積極性

用低積極性問一個簡單問題：「200 的 15% 是多少？」會立即得到直接答案。再用高積極性問複雜問題：「為高流量 API 設計快取策略」。點擊 **🔴 串流回應（即時）**，你會看到模型一字一句詳細推理。相同模型、相同問題結構，提示告訴它需要思考的深度。

### 任务执行（工具前置說明）

多步驟工作流程受惠於提前規劃和進度說明。模型會先說明它將執行什麼，每一步進行解說，最後總結結果。

### 自我反思程式碼

試試「建立一個電子郵件驗證服務」。模型不只是生成代碼然後停止，而是生成後依照品質標準評估，識別弱點並改進。你會看到它反覆迭代直到代碼達到生產級標準。

### 結構化分析

程式碼審查需要一致的評估框架。模型依固定類別（正確性、實務、效能、安全性）進行分析，並劃分嚴重程度。

### 多輪對話

問「什麼是 Spring Boot？」接著立即問「給我一個範例」。模型會記住第一個問題，並針對 Spring Boot 提供具體範例。沒有記憶功能時，第二個問題太模糊。

### 步驟推理

挑個數學題，用步驟推理和低積極性兩種方式試試。低積極性快速給答案，但過程不透明。步驟推理則顯示每一步的計算與決策。

### 限制輸出

需要特定格式或字數時，這種模式強制嚴格遵守。試著生成一個正好 100 字、以項目符號列出的總結。

## 你真正學到的是

**推理努力改變一切**

GPT-5.2 讓你能透過提示控制計算努力。低努力意味快速回答，探索範圍小。高努力意味模型花時間深入思考。你學會依任務複雜度調整努力——簡單問題不浪費時間，複雜決策不草率。

**結構引導行為**

注意提示中的 XML 標籤？它們不是裝飾。模型更可靠地遵守結構化指令，而非自由文本。需要多步驟流程或複雜邏輯時，結構幫助模型追蹤當前狀態與接下來要做的事。

<img src="../../../translated_images/zh-MO/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*一個結構良好的提示組成，有明確區塊和 XML 風格組織*

**透過自我評價提升品質**

自我反思模式是透過明確品質標準實現。不是希望模型「做好」而已，而是明確告訴它「做好」的定義：正確邏輯、錯誤處理、效能、安全性。模型能評估並改善輸出。這讓代碼生成變成一個過程，而非純粹靠運氣。

**上下文是有限的**

多輪對話靠包含訊息歷史來實現。但有最大 token 限制。隨著對話增加，需要策略來保持相關上下文且不超限。本模組展示記憶如何運作，之後你會學會何時摘要、何時忘記、何時取回。

## 下一步

**下一模組：** [03-rag - RAG (檢索增強生成)](../03-rag/README.md)

---

**導覽：** [← 上一章：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一章：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用AI翻譯服務[Co-op Translator](https://github.com/Azure/co-op-translator)進行翻譯。雖然我們致力確保準確性，請注意自動翻譯可能含有錯誤或不準確之處。原始文件之原文版本應被視為權威資料來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而引致之任何誤解或錯誤詮釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->