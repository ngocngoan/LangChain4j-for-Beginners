# Module 02: 使用 GPT-5.2 進行提示工程

## 目錄

- [您將學習什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [了解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次示例提示](../../../02-prompt-engineering)
  - [少次示例提示](../../../02-prompt-engineering)
  - [思維鏈](../../../02-prompt-engineering)
  - [角色基礎提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低慾望 vs 高慾望](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [限制輸出](../../../02-prompt-engineering)
- [您真正學習的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 您將學習什麼

<img src="../../../translated_images/zh-TW/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

在上一個模組中，您瞭解了記憶如何促進對話式 AI，並使用 GitHub 模型進行基本互動。現在，我們將專注於您如何提問——也就是提示本身——使用 Azure OpenAI 的 GPT-5.2。您如何結構提示會極大影響您得到的回應品質。我們從複習基本提示技巧開始，然後介紹八個進階模式，充分利用 GPT-5.2 的功能。

我們選擇 GPT-5.2 是因為它引入了推理控制——您可以告訴模型在回答前要思考多少。這使得不同提示策略更明顯，也幫助您理解何時使用每種方法。我們還會受益於 Azure 對 GPT-5.2 的較少速率限制，與 GitHub 模型相比有優勢。

## 先決條件

- 已完成模組 01（Azure OpenAI 資源已部署）
- 根目錄下有含 Azure 憑證的 `.env` 檔案（由模組 01 中的 `azd up` 創建）

> **注意：** 如果您尚未完成模組 01，請先依指示完成部署。

## 了解提示工程

<img src="../../../translated_images/zh-TW/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

提示工程是設計輸入文本以穩定獲得所需結果的技術。它不只是問問題——而是構造請求，讓模型完全理解您想要什麼以及如何提供。

想像您在給同事指示。單說「修復錯誤」太含糊。「在 UserService.java 第45行對空指標異常加個空值檢查來修復」則具體。語言模型也是如此——具體且有結構的提示非常重要。

<img src="../../../translated_images/zh-TW/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j 提供基礎設施——模型連接、記憶和訊息類型——而提示模式就是透過這個基礎設施傳送的精心結構化文本。關鍵組件是 `SystemMessage`（設定 AI 行為與角色）和 `UserMessage`（承載您的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-TW/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

在進入本模組的進階模式之前，先回顧五個基礎提示技術。這些是每位提示工程師必須了解的基礎組件。如果您已經學過[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，您應該已經看過它們的實作——這裡提供它們的概念框架。

### 零次示例提示

最簡單的方法：給模型直接指令，不帶示例。模型完全依賴訓練來理解且執行任務。這適合明確且直接的請求。

<img src="../../../translated_images/zh-TW/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*直接指令沒有示例——模型從指令推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應:「正面」
```

**何時使用：** 簡單分類、直接問句、翻譯，或模型不需額外指導即可處理的任務。

### 少次示例提示

提供示例以展示您希望模型遵循的模式。模型從示例學習預期輸入輸出格式，並應用於新輸入。大幅提升目標格式或行為不明顯任務的一致性。

<img src="../../../translated_images/zh-TW/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*從示例學習——模型識別模式並套用於新輸入*

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

**何時使用：** 自訂分類、一致格式化、特定領域任務，或零次示例結果不穩定時。

### 思維鏈

要求模型一步步顯示推理過程。模型不直接跳到答案，而是拆解問題並逐步處理。提升數學、邏輯與多步推理任務的準確度。

<img src="../../../translated_images/zh-TW/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*逐步推理——將複雜問題拆解為明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**何時使用：** 數學題、邏輯謎題、調試，或任何顯示推理過程可提升準確性與信任的任務。

### 角色基礎提示

在提出問題前設定 AI 的角色或身分。這提供背景，影響回答的口吻、深度與重點。「軟體架構師」與「新手開發者」或「安全稽核員」給出的建議會不同。

<img src="../../../translated_images/zh-TW/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*設定背景與角色——相同問題依分配角色產生不同回應*

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

**何時使用：** 程式碼審查、教學、特定領域分析，或需要針對專業層級或視角量身定制回答時。

### 提示模板

建立可重複使用且帶變數佔位符的提示。無需每次都寫新提示，只需定義一次模板並填入不同值。LangChain4j 的 `PromptTemplate` 類用 `{{variable}}` 語法使此更簡單。

<img src="../../../translated_images/zh-TW/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*帶變數佔位符的可重複使用提示——一個模板，多種應用*

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

**何時使用：** 多次查詢但輸入不同、批次處理、建立可重用 AI 工作流，或提示結構不變但資料變動的場景。

---

這五個基礎提供了大部分提示任務的堅實工具。此模組其餘內容基於它們，加入了 **八個進階模式**，利用 GPT-5.2 的推理控制、自我評估與結構化輸出能力。

## 進階模式

打下基礎後，來看看使本模組獨特的八個進階模式。不是所有問題都用同樣方法。有些問題需要快速回答，有些需要深度思考。有些需要顯示推理過程，有些只要結果。以下每個模式針對不同場景優化——GPT-5.2 的推理控制讓這些差異更加明顯。

<img src="../../../translated_images/zh-TW/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八種提示工程模式及其應用概覽*

<img src="../../../translated_images/zh-TW/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 的推理控制可指定模型思考深度——從快速直接回答到深度探索*

**低慾望（快速且專注）** - 用於簡單問題，需要快速、直接回答。模型進行最少推理——最多兩步。適用於計算、查詢或直接問答。

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

> 💡 **使用 GitHub Copilot 探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 並詢問：
> - 「低慾望與高慾望提示模式有何不同？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「何時應使用自我反思模式，何時使用直接指令？」

**高慾望（深入且徹底）** - 用於複雜問題，需要全面分析。模型深入探索並展示詳盡推理。適用系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** - 適用多步工作流程。模型提供前置計劃，邊執行邊說明每步，最後給出摘要。用於遷移、實作或其他多步驟流程。

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

思維鏈提示明確要求模型展示推理過程，提升複雜任務準確度。逐步拆解讓人與 AI 均能理解邏輯。

> **🤖 嘗試 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問此模式：
> - 「如何為長時間運行作業調整任務執行模式？」
> - 「生產環境中如何最佳結構化工具前言？」
> - 「如何捕捉並在 UI 中顯示中間進度更新？」

<img src="../../../translated_images/zh-TW/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*計劃 → 執行 → 總結多步任務工作流程*

**自我反思代碼** - 用於產生符合生產標準的代碼。模型依產業標準產生含適當錯誤處理的高品質代碼。用於建立新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-TW/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*迭代改進循環——生成、評估、識別問題、改良、重複*

**結構化分析** - 用於一致性評估。模型以固定框架審核代碼（正確性、實務、效能、安全性、可維護性）。用於程式碼審查或品質評估。

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

> **🤖 嘗試 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問結構化分析：
> - 「如何針對不同類型代碼審查客製化分析框架？」
> - 「以程式化方式解析並處理結構化輸出，有何最佳做法？」
> - 「如何確保不同審查會議間嚴重性等級的一致性？」

<img src="../../../translated_images/zh-TW/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*用於一致程式碼審查及嚴重性分類的框架*

**多輪對話** - 用於需上下文的對話。模型記憶前訊息並在此基礎回應。適用於互動輔助或複雜問答。

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

*對話上下文在多輪間累積直至達標記限制*

**逐步推理** - 用於需展現邏輯的問題。模型明確展出每步推理。適合數學題、邏輯謎題，或需理解思考過程時。

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

*將問題拆解為明確邏輯步驟*

**限制輸出** - 適用於需遵守特定格式要求的回應。模型嚴格遵循格式及長度規則。適用摘要或需精確輸出結構時。

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

*強制執行特定格式、長度與結構要求*

## 使用現有 Azure 資源

**驗證部署：**

確保根目錄中存在含 Azure 憑證的 `.env` 檔案（在模組 01 期間建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果您已使用模組 01 中的 `./start-all.sh` 啟動所有應用程式，本模組已於 8083 埠上運行。您可以跳過以下啟動指令，直接前往 http://localhost:8083。

**選項 1：使用 Spring Boot Dashboard（建議 VS Code 使用者）**

開發容器已包含 Spring Boot Dashboard 擴充，提供視覺化界面管理所有 Spring Boot 應用程式。您可在 VS Code 左側活動列找到（尋找 Spring Boot 圖示）。

透過 Spring Boot Dashboard，您可以：
- 查看工作區中所有可用 Spring Boot 應用
- 一鍵啟動/停止應用程式
- 即時檢視應用日誌
- 監控應用狀態
只需點擊「prompt-engineering」旁的播放按鈕即可啟動此模組，或一次啟動所有模組。

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

兩個腳本會自動從根目錄的 `.env` 檔載入環境變數，並在 JAR 不存在時編譯它們。

> **注意：** 如果您想在啟動前手動編譯所有模組：
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

在瀏覽器中開啟 http://localhost:8083 。

**停止指令：**

**Bash:**
```bash
./stop.sh  # 僅限此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-TW/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主儀表板展示所有 8 種 prompt 設計模式以及它們的特性和應用案例*

## 探索這些模式

這個網頁介面讓你試驗不同的提示策略。每種模式解決不同問題——試試看它們，了解每種方法的最佳使用時機。

> **注意：串流與非串流** — 每個模式頁面都有兩個按鈕：**🔴 即時串流回應（Live）** 和 **非串流** 選項。串流使用伺服器推送事件（SSE）即時顯示模型生成的字元，所以你會立即看到進度。非串流則會等整個回應完成後才顯示。對於需要深入推理的提示（例如，高積極性、反思式程式碼），非串流呼叫可能花很長時間——有時甚至幾分鐘，且沒有任何顯示回饋。**嘗試複雜提示時建議使用串流**，這樣你才能看到模型運作，並避免誤以為請求已逾時。
>
> **注意：瀏覽器需求** — 串流功能使用 Fetch Streams API（`response.body.getReader()`），需要完整版瀏覽器（Chrome、Edge、Firefox、Safari）。VS Code 內建的 Simple Browser 不支援此 API，因此無法串流。若使用 Simple Browser，非串流按鈕仍正常運作，只有串流按鈕會受到影響。建議在外部瀏覽器中開啟 `http://localhost:8083` 以獲得完整體驗。

### 低積極性 vs 高積極性

用低積極性問一個簡單問題，比如「200 的 15% 是多少？」你會迅速得到直接答案。接著用高積極性問一個複雜問題，如「設計高流量 API 的快取策略」。點擊 **🔴 即時串流回應（Live）**，觀察模型以字元為單位逐步產生詳細推理結果。模型與問題結構相同，但提示告訴它要思考多少。

### 任務執行（工具預備語）

多步驟工作流程受益於事前規劃與過程解說。模型會先列出它將執行的內容，逐步解說每一步，最後總結結果。

### 反思式程式碼

嘗試「建立一個電子郵件驗證服務」。模型不只是生成程式碼後停止，它會生成後評估品質標準，找出缺點並改進。你會看到它反覆迭代直到程式碼達到生產標準。

### 結構化分析

程式碼審查需要一致的評估框架。模型使用固定類別（正確性、最佳實踐、效能、安全性）與嚴重程度來分析程式碼。

### 多回合聊天

問「什麼是 Spring Boot？」接著馬上追問「給我一個範例」。模型會記得你的第一個問題，並專門提供一個 Spring Boot 範例。如果沒有記憶，第二個問題會太模糊。

### 逐步推理

選一個數學問題，分別使用逐步推理和低積極性來嘗試。低積極性快速給答案，但不透明；逐步推理則展示每個計算和決策。

### 受限輸出

當你需要特定格式或字數時，此模式強制嚴格遵守。試著產生一篇精確 100 字的摘要，以項目符號表示。

## 你真正學到的是什麼

**推理的努力改變一切**

GPT-5.2 讓你可透過提示控制計算努力程度。努力低代表快速回應，探索少。努力高代表模型花時間深入思考。你在學習如何依任務複雜度匹配努力——簡單問題不浪費時間，複雜決策也不需急躁。

**結構引導行為**

注意 prompt 中的 XML 標籤？它們不是裝飾。模型更可靠地遵守結構化指令，而非自由文本。需要多步驟流程或複雜邏輯時，結構幫助模型追蹤目前階段與接下來流程。

<img src="../../../translated_images/zh-TW/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*清楚分段且具 XML 風格組織的 prompt 結構解析*

**透過自我評估確保品質**

反思式模式具體說明品質標準。你不是希望模型「做對」，而是明確告訴它「對」的定義：邏輯正確、錯誤處理、效能、安全。模型才能自行評價輸出並改進。這讓程式碼產生從隨機變成有程序可循。

**上下文有限**

多回合對話是透過每次請求帶入訊息歷史實現，但有限制——每個模型都有最大 token 數。對話變長時，你需要策略保留重要上下文、避免超出限制。這個模組教你記憶如何運作；後面會學何時摘要、何時忘記、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG (檢索增強生成)](../03-rag/README.md)

---

**導覽：** [← 上一頁：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一頁：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們致力於提高準確度，但請注意，機器翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->