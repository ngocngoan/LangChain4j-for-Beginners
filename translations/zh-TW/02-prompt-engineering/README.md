# 模組 02：使用 GPT-5.2 進行提示工程

## 目錄

- [影片導覽](../../../02-prompt-engineering)
- [你將學習什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少量示例提示](../../../02-prompt-engineering)
  - [思維鏈](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低熱忱 vs 高熱忱](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到什麼](../../../02-prompt-engineering)
- [接下來的步驟](../../../02-prompt-engineering)

## 影片導覽

觀看此直播課程，說明如何開始本模組：[使用 LangChain4j 進行提示工程 - 直播課程](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## 你將學習什麼

<img src="../../../translated_images/zh-TW/what-youll-learn.c68269ac048503b2.webp" alt="你將學習什麼" width="800"/>

在上一模組中，你了解了記憶如何促進對話式 AI，並使用 GitHub 模型進行基本互動。現在，我們將專注於如何提問——也就是提示本身——使用 Azure OpenAI 的 GPT-5.2。你如何結構你的提示，將會大幅影響你獲得的回應品質。我們從提示技術基礎開始回顧，然後進入八種進階模式，充分利用 GPT-5.2 的能力。

我們使用 GPT-5.2 是因為它引入了推理控制——你可以告訴模型在回答前應進行多少思考。這讓不同的提示策略更明顯，幫助你了解何時使用各種方法。我們也將受益於 Azure 對 GPT-5.2 較少的速率限制，相較於 GitHub 模型。

## 先決條件

- 完成模組 01（Azure OpenAI 資源已部署）
- 根目錄下有 `.env` 檔案，包含 Azure 認證（由模組 01 中的 `azd up` 建立）

> **注意：** 若尚未完成模組 01，請先依照那裡的部署說明操作。

## 理解提示工程

<img src="../../../translated_images/zh-TW/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文本，使你持續獲得所需結果的過程。這不只是提出問題，而是結構化請求，讓模型完全理解你的需求以及如何達成。

把它想像成給同事下指令。「修復錯誤」很籠統。「在 UserService.java 第 45 行修復 null 指標例外，透過新增 null 檢查」則具體明確。語言模型運作也一樣——具體性和結構化很重要。

<img src="../../../translated_images/zh-TW/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的作用" width="800"/>

LangChain4j 提供基礎架構——模型連接、記憶與訊息型別——而提示模式只是你透過該架構傳送的精心結構化文字。核心建構積木是 `SystemMessage`（設定 AI 行為與角色）和 `UserMessage`（承載你實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-TW/five-patterns-overview.160f35045ffd2a94.webp" alt="五種基礎提示工程模式總覽" width="800"/>

在進入本模組的進階模式之前，讓我們回顧五種基礎提示技術。這些是每位提示工程師都應該了解的基石。如果你已經閱讀過[快速開始模組](../00-quick-start/README.md#2-prompt-patterns)，你應該看過它們的實作——這裡是它們背後的概念架構。

### 零次提示

最簡單的方法：直接給模型指令，沒有範例。模型完全依靠訓練來理解和執行任務。這適用於期待行為明確的簡單請求。

<img src="../../../translated_images/zh-TW/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次提示" width="800"/>

*無範例的直接指令——模型僅從指令推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應:「正面」
```

**何時使用：** 簡單分類、直接提問、翻譯，或任何模型能無需額外指引即可處理的任務。

### 少量示例提示

提供示例演示你希望模型遵循的模式。模型從範例中學習預期的輸入輸出格式，並套用至新輸入。在想要格式或行為不明確的任務中，這方法大幅提升一致性。

<img src="../../../translated_images/zh-TW/few-shot-prompting.9d9eace1da88989a.webp" alt="少量示例提示" width="800"/>

*從示例學習——模型識別模式並應用於新輸入*

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

**何時使用：** 自訂分類、一致格式、特定領域任務，或零次提示結果不一致時。

### 思維鏈

要求模型逐步展示推理。模型不直接給答案，而是分解問題，逐步敘述。這改善數學、邏輯及多步推理任務的準確性。

<img src="../../../translated_images/zh-TW/chain-of-thought.5cff6630e2657e2a.webp" alt="思維鏈提示" width="800"/>

*逐步推理——將複雜問題拆解成明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**何時使用：** 數學題、邏輯謎題、除錯，或需要推理過程以提升準確性與信任的任務。

### 基於角色的提示

在提問前設定 AI 的角色或身份。這提供上下文，塑造回答的語氣、深度與焦點。譬如「軟體架構師」和「初級開發者」或「安全稽核員」的建議會不同。

<img src="../../../translated_images/zh-TW/role-based-prompting.a806e1a73de6e3a4.webp" alt="基於角色的提示" width="800"/>

*設定上下文與身份——同一問題依指定角色得到不同回應*

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

**何時使用：** 程式碼審查、教學、特定領域分析，或需要根據專業水準或觀點訂製回應時。

### 提示模板

建立可重複使用且帶變數占位符的提示。無需每次重寫提示，定義模板一次，填入不同值。LangChain4j 的 `PromptTemplate` 類提供方便的 `{{variable}}` 語法。

<img src="../../../translated_images/zh-TW/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*帶變數占位符的可重用提示——一個模板，多種用途*

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

**何時使用：** 重複查詢不同輸入、批次處理、建立可重用 AI 工作流程，或任何提示結構固定但資料變動的場景。

---

這五種基礎提供你應對大多數提示任務的堅實工具組。本模組其餘內容基於此，加入**八種進階模式**，充分利用 GPT-5.2 的推理控制、自我評估與結構化輸出能力。

## 進階模式

基礎介紹完後，讓我們進入本模組獨具特色的八種進階模式。不同問題不需要相同方式。有些問題需要快速回答，有些需要深入思考。有些需要可見推理，有些只要結果。以下每種模式都為特定場景最佳化——而 GPT-5.2 的推理控制更凸顯差異。

<img src="../../../translated_images/zh-TW/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示工程模式" width="800"/>

*八種提示工程模式及其使用情境總覽*

<img src="../../../translated_images/zh-TW/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 的推理控制讓你指定模型思考程度——從快速直接回應到深入探討*

**低熱忱（快速且聚焦）** - 適用於你想要快速直接答案的簡單問題。模型進行最少推理——最多兩個步驟。用於計算、查詢或簡單問題。

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

> 💡 **用 GitHub Copilot 探索：** 打開 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 並詢問：
> - 「低熱忱與高熱忱提示模式有何不同？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「何時使用自我反思模式，何時用直接指令？」

**高熱忱（深入且徹底）** - 適用於需要全面分析的複雜問題。模型深入探討並展示詳細推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步推進）** - 適用於多步工作流程。模型提供計劃，執行時步驟解說，最後給出總結。用於遷移、實作或任何多步驟流程。

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

思維鏈提示明確告訴模型展現推理過程，提升複雜任務的準確度。逐步細分有助人類與 AI 共同理解邏輯。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 詢問此模式相關內容：
> - 「如何為長時間運作的操作調整任務執行模式？」
> - 「生產環境中工具前言的結構最佳實踐是什麼？」
> - 「如何在使用者介面中捕捉並展示中間進度更新？」

<img src="../../../translated_images/zh-TW/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結的多步工作流程*

**自我反思代碼** - 用於產出符合生產標準的程式碼。模型生產的代碼有適當錯誤處理。用於構建新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-TW/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思流程" width="800"/>

*反覆優化循環——生成、評估、找出問題、改進、重複*

**結構化分析** - 用於一致的評估。模型用固定框架（正確性、實務、效能、安全性、維護性）審視代碼。用於程式碼審查或品質評估。

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

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 詢問結構化分析：
> - 「如何自訂不同類型程式碼審查的分析框架？」
> - 「程式化解析並運用結構化輸出的最佳方法是什麼？」
> - 「如何確保不同審查會議的一致嚴重性等級？」

<img src="../../../translated_images/zh-TW/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*具嚴重性等級的一致程式碼審查框架*

**多輪對話** - 適用於需要上下文的對話。模型記住先前訊息並基於它們回應。用於互動式協助或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-TW/context-memory.dff30ad9fa78832a.webp" alt="對話上下文記憶" width="800"/>

*對話上下文在多輪中累積，直到達到 token 限制*

**逐步推理** - 適用需顯示邏輯的問題。模型對每步驟都展示明確推理。用於數學題、邏輯謎題，或需理解思考過程的情況。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-TW/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="逐步推理模式" width="800"/>

*將問題拆解成明確邏輯步驟*

**受限輸出** - 適用需特定格式要求的回應。模型嚴格遵守格式與長度規則。用於摘要或需要精確輸出結構的場合。

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

<img src="../../../translated_images/zh-TW/constrained-output-pattern.0ce39a682a6795c2.webp" alt="受限輸出模式" width="800"/>

*強制特定格式、長度及結構要求*

## 使用現有 Azure 資源

**驗證部署：**

確認根目錄下存在 `.env` 檔案，包含 Azure 認證（模組 01 部署時建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若你已使用模組 01 的 `./start-all.sh` 啟動所有應用，本模組已在埠號 8083 運行。你可以跳過以下啟動指令，直接開啟 http://localhost:8083 。

**方案一：使用 Spring Boot 儀表板（推薦 VS Code 使用者）**
開發容器包含 Spring Boot Dashboard 擴充功能，提供管理所有 Spring Boot 應用程式的視覺介面。你可以在 VS Code 左側的活動列中找到它（尋找 Spring Boot 圖示）。

在 Spring Boot Dashboard 中，你可以：
- 查看工作區中所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 以即時方式查看應用程式日誌
- 監控應用程式狀態

只需點擊「prompt-engineering」旁的播放按鈕即可啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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

兩個腳本會自動從根目錄的 `.env` 檔案載入環境變數，若 JAR 檔不存在則會建置。

> **注意：** 如果你想在啟動前手動建置所有模組：
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

在瀏覽器中打開 http://localhost:8083 。

**停止方法：**

**Bash:**
```bash
./stop.sh  # 只有此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只有此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-TW/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主控台顯示所有 8 種 prompt engineering 範例及其特性與使用案例*

## 探索模式

網頁介面讓你嘗試不同提示策略。每個模式解決不同問題—試試看，了解每種方法何時最有效。

> **注意：串流與非串流** — 每個模式頁面提供兩個按鈕：**🔴 串流回應 (即時)** 和 **非串流** 選項。串流使用伺服器傳送事件（SSE），讓你即時看到模型產生的每個字彙，進度一目瞭然。非串流則會等整個回應完成後才顯示。針對需要深入推理的提示（如高積極性、反思式程式碼），非串流呼叫可能耗時非常長—有時甚至分鐘—且無即時回饋。**在嘗試複雜提示時請使用串流**，這樣你可以看到模型運作過程，避免誤以為請求超時。
>
> **注意：瀏覽器限制** — 串流功能採用 Fetch Streams API (`response.body.getReader()`) ，需要完整瀏覽器（Chrome、Edge、Firefox、Safari）。VS Code 內建的簡易瀏覽器因其 webview 不支援 ReadableStream API，無法使用此功能。如果使用簡易瀏覽器，非串流按鈕仍正常，只是串流按鈕不會生效。建議在外部瀏覽器中打開 `http://localhost:8083` 以獲得完整體驗。

### 低積極性 vs 高積極性

用低積極性模式問個簡單問題，例如：「200 的 15% 是多少？」你會立刻得到直接答案。再用高積極性問複雜問題，例如：「設計一個高流量 API 的快取策略。」點擊 **🔴 串流回應 (即時)**，觀看模型逐字呈現其詳細推理。相同模型、相同問題結構，提示決定了模型要投入多少思考。

### 任務執行（工具前置提示）

多步驟工作流程受益於預先規劃與進度敘述。模型會列出要做的事，敘述每步，最後總結結果。

### 反思式程式碼

試試「建立一個電子郵件驗證服務」。模型不只是生成程式碼後停下，還會生成、根據品質標準評估、找出弱點並改進。你會看到它反覆迭代，直到程式碼達到生產標準。

### 結構化分析

程式碼審查需要一致的評估框架。模型會用固定類別（正確性、實作、效能、安全）並具備嚴重度等級分析程式碼。

### 多回合對話

問「什麼是 Spring Boot？」接著緊接著問「給我一個範例」。模型記得你第一個問題，並針對 Spring Boot 提供範例。沒有記憶，第二個問題將太過模糊。

### 逐步推理

挑個數學題，用逐步推理和低積極性分別嘗試。低積極性直接給答案—快速但不透明。逐步推理會展示每個計算與決策過程。

### 受限輸出

當你需要特定格式或字數時，此模式會嚴格遵守。試試生成一段正好 100 字且以項目符號格式呈現的摘要。

## 你真正學到的是什麼

**推理努力改變一切**

GPT-5.2 讓你透過提示控制運算努力。低努力代表快速反應，探索有限。高努力代表模型花時間深度思考。你學會根據任務複雜度調整努力—簡單問題別浪費時間，複雜決策也別趕。

**結構引導行為**

注意提示裡的 XML 標籤嗎？它們不是裝飾。模型比起自由文本，更能可靠遵從結構化指令。當你需要多步驟流程或複雜邏輯時，結構能幫助模型追蹤目前狀態和接下步驟。

<img src="../../../translated_images/zh-TW/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*具清晰區段與 XML 風格組織的良好結構化提示剖析*

**品質來自自我評估**

反思式模式透過明確品質標準達成效果。你不是祈禱模型「做對」，而是直接告訴它「對」的標準：邏輯正確、錯誤處理、效能、安全。模型接著可自我評估並改進。這讓程式碼生成從樂透變成流程。

**上下文有限**

多回合對話透過每次請求包含訊息歷史實現。但有限制—每個模型有最大 Token 數量。隨對話成長，你必須用策略保持相關上下文而不超限。本模組教你記憶工作原理；接著你會學會何時摘要、何時忘記、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG (檢索增強生成)](../03-rag/README.md)

---

**導航：** [← 上一頁：模組 01 - 介紹](../01-introduction/README.md) | [返回主頁](../README.md) | [下一頁：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件經由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保翻譯的準確性，但請注意，自動翻譯可能包含錯誤或不精確之處。原始文件的母語版本應視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯所引起的任何誤解或誤譯負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->