# 模組 02：使用 GPT-5.2 的提示工程

## 目錄

- [影片導覽](../../../02-prompt-engineering)
- [您將學習什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少量提示](../../../02-prompt-engineering)
  - [思維鏈](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示範本](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有的 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低渴望與高渴望](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多回合對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 影片導覽

觀看此直播，了解如何開始本模組：

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## 您將學習什麼

<img src="../../../translated_images/zh-TW/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

在前一個模組中，您看到記憶如何使對話式 AI 成為可能，並使用 GitHub 模型進行基本互動。現在我們將專注於如何提問──也就是提示本身──使用 Azure OpenAI 的 GPT-5.2。您如何結構化提示會極大影響您得到的回答品質。我們將從基礎提示技術回顧開始，然後進入八個進階模式，充分利用 GPT-5.2 的功能。

我們使用 GPT-5.2，是因為它引入了推理控制——您可以告訴模型在回答前需要多少思考。這使不同的提示策略更明顯，也幫助您了解何時使用哪種方法。相較於 GitHub 模型，我們還將受益於 Azure 對 GPT-5.2 實施較少的速率限制。

## 先決條件

- 完成模組 01（部署 Azure OpenAI 資源）
- 根目錄下有包含 Azure 憑證的 `.env` 檔案（模組 01 中由 `azd up` 建立）

> **注意：** 如果您尚未完成模組 01，請先依據該模組的部署指示操作。

## 理解提示工程

<img src="../../../translated_images/zh-TW/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

提示工程是設計輸入文字，使您持續獲得所需結果的藝術。它不僅是提問──而是結構化請求，使模型能精確理解您的需求及如何執行。

可將其想像為對同事下指令。「修正錯誤」太過模糊。「透過加入 null 檢查修正 UserService.java 第 45 行的空指標例外」則相當具體。語言模型也是如此──具體性與結構很重要。

<img src="../../../translated_images/zh-TW/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j 提供基礎設施──模型連線、記憶體和訊息類型──而提示模式則是您透過該基礎設施傳送的經過精心結構化的文字。關鍵組成部分是 `SystemMessage`（設定 AI 的行為與角色）和 `UserMessage`（帶有您的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-TW/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

在進入本模組的進階模式之前，讓我們回顧五種基礎提示技術。這是每位提示工程師都應知道的基石。如果您已經體驗過[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，您應該見過這些實作──以下是背後的概念框架。

### 零次提示

最簡單的方法：給模型直接指令，沒有範例。模型完全依靠訓練理解和執行任務。這對預期行為明顯的簡單請求效果良好。

<img src="../../../translated_images/zh-TW/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*無範例直接指令──模型僅根據指令推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應:「正面」
```

**使用時機：** 簡單分類、直接提問、翻譯，或任何模型能無需額外指引處理的任務。

### 少量提示

提供範例，示範您希望模型遵循的模式。模型自範例中學習期望的輸入輸出格式，並將它套用到新輸入上。這大幅提升欲呈現格式或行為不明顯任務的一致性。

<img src="../../../translated_images/zh-TW/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*從範例中學習──模型識別模式並套用於新輸入*

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

**使用時機：** 自訂分類、一致格式、特定領域任務，或零次結果不穩定時。

### 思維鏈

要求模型逐步展示推理過程。模型不直接跳到答案，而是將問題拆解並逐一解決。這能提升數學、邏輯、多步推理任務的準確性。

<img src="../../../translated_images/zh-TW/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*逐步推理──將複雜問題拆成明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**使用時機：** 數學問題、邏輯謎題、除錯，或任何展示推理過程能提升準確性和信任的任務。

### 基於角色的提示

在提問前設定 AI 的角色或身份。這為回答提供背景，影響語氣、深度和聚焦。 「軟體架構師」給的建議，與「初級開發者」或「資安稽核員」截然不同。

<img src="../../../translated_images/zh-TW/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*設定上下文和角色──同一問題在不同角色下獲得不同回應*

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

**使用時機：** 程式碼審查、教學、領域專門分析，或需要依專業層級和觀點調整回答時。

### 提示範本

建立可重用的提示範本，內含變數占位符。每次不用重寫新提示，只需定義一次範本並填入不同的值。LangChain4j 的 `PromptTemplate` 類使用 `{{variable}}` 語法讓這很簡單。

<img src="../../../translated_images/zh-TW/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*帶變數占位符的可重用提示──一份範本，多種用法*

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

**使用時機：** 不同輸入的重複查詢、批次處理、建立可重用 AI 工作流程，或結構相同而資料不同的場景。

---

這五項基礎技巧為大多數提示任務提供堅實工具。接下來，本模組會在此基礎上，利用 GPT-5.2 的推理控制、自我評估及結構化輸出能力，展開**八個進階模式**。

## 進階模式

基礎介紹完畢，讓我們進入本模組獨特的八個進階模式。並非所有問題都適用同一方法。有些問題需要快速回答，有些則需深入思考。有些需展現推理，有些只求結果。以下每種模式針對不同場景優化——而 GPT-5.2 的推理控制使差異更顯著。

<img src="../../../translated_images/zh-TW/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八種提示工程模式及其使用場合概覽*

<img src="../../../translated_images/zh-TW/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 的推理控制讓您指定模型應思考多少——從快速直接回覆到深入探究*

**低渴望（快速且專注）** - 適用需要快速直接答案的簡單問題。模型採用最少推理——最多兩步。用於計算、查詢或直白問題。

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

> 💡 **在 GitHub Copilot 中探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)，詢問：
> - 「低渴望與高渴望提示模式有何差異？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「何時應使用自我反思模式而非直接指令？」

**高渴望（深度且全面）** - 適合需要完整分析的複雜問題。模型徹底探討並展示細節推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進行）** - 用於多步工作流程。模型先擬訂計劃，邊做邊敘述，最後總結。適用遷移、實作或任何多步驟過程。

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

思維鏈提示明確要求模型展示推理過程，提升複雜任務的準確度。逐步拆解對人類與 AI 均有助理解邏輯。

> **🤖 試試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 詢問此模式：
> - 「如何調整任務執行模式用於長時間作業？」
> - 「生產環境中結構化工具前言的最佳實踐為何？」
> - 「如何捕捉並在 UI 中顯示中間進度更新？」

<img src="../../../translated_images/zh-TW/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*計劃 → 執行 → 總結，適用多步任務工作流程*

**自我反思代碼** - 用於生成生產品質的程式碼。模型依生產標準生成，並具備適當錯誤處理。用於新功能或服務開發。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-TW/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*迭代改進循環──生成、評估、找問題、改進、重複*

**結構化分析** - 用於一致性評估。模型以固定框架（正確性、實踐、效能、安全性、可維護性）審查程式碼。用於程式碼審查或品質評估。

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

> **🤖 試試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 詢問結構化分析：
> - 「如何為不同類型程式碼審查自訂分析框架？」
> - 「用程式方式解析並處理結構化輸出的最佳方式？」
> - 「如何確保不同審查階段中嚴重程度的一致？」

<img src="../../../translated_images/zh-TW/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*用嚴重程度分級，建立一致的程式碼審查框架*

**多回合對話** - 適用需要上下文的長談。模型記得過往訊息並基於此繼續。適用互動輔助或複雜問答。

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

*多回合中會話上下文逐步累積，直至達到 token 限制*

**逐步推理** - 用於需展現明確邏輯的問題。模型展示每步推理過程。用於數學問題、邏輯謎題，或需理解思考過程。

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

**受限輸出** - 用於回應格式具體要求。模型嚴格遵守格式與長度規則。用於摘要或需精確輸出結構時。

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

*強制指定格式、長度及結構要求*

## 使用現有的 Azure 資源

**驗證部署：**

確認根目錄存在 `.env` 檔案，且含 Azure 憑證（模組 01 期間建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果您已使用模組 01 的 `./start-all.sh` 啟動所有應用程式，本模組已在 8083 埠執行。您可以跳過下方啟動指令，直接前往 http://localhost:8083 。
**選項 1：使用 Spring Boot Dashboard（推薦給 VS Code 使用者）**

開發容器包含了 Spring Boot Dashboard 擴充套件，它提供一個視覺化介面來管理所有 Spring Boot 應用程式。你可以在 VS Code 左側的活動列中找到它（尋找 Spring Boot 圖示）。

從 Spring Boot Dashboard，你可以：
- 查看工作區中所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 實時查看應用程式日誌
- 監控應用程式狀態

只要點擊 "prompt-engineering" 旁邊的播放按鈕即可啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有 Web 應用程式（模組 01-04）：

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

或者只啟動此模組：

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

這兩個腳本都會自動從根目錄的 `.env` 檔案載入環境變數，且如果 JAR 檔不存在就會編譯建構。

> **注意：** 如果你喜歡在啟動前手動建構所有模組：
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

請在瀏覽器中打開 http://localhost:8083 。

**停止：**

**Bash:**
```bash
./stop.sh  # 僅限此模組
# 或
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

*主儀表板顯示所有 8 種提示工程範例及其特性和使用案例*

## 探索模式

這個 Web 介面讓你可以試驗不同的提示策略。每種模式解決不同的問題—試用它們，看看何時哪種方法效果最好。

> **注意：串流與非串流** — 每個模式頁面提供兩個按鈕：**🔴 串流回應（即時）** 與 **非串流** 選項。串流使用 Server-Sent Events (SSE) 即時顯示模型產生的 token，可即時看到進度。非串流選項會等完整回應產生好後再顯示。對於觸發深度推理的提示（例如高熱情度、具自我反思的程式碼），非串流的呼叫可能需要非常長時間—有時甚至幾分鐘—且沒有可見回饋。**實驗複雜提示時建議使用串流**，這樣可以看到模型運作並避免誤以為請求逾時。
>
> **注意：瀏覽器要求** — 串流功能使用 Fetch Streams API (`response.body.getReader()`) ，需要完整瀏覽器支援（Chrome、Edge、Firefox、Safari）。在 VS Code 內建的簡易瀏覽器中不適用，因其 webview 不支援 ReadableStream API。如果你使用簡易瀏覽器，非串流按鈕仍可正常使用—只有串流按鈕會受影響。想獲得完整體驗，請在外部瀏覽器打開 `http://localhost:8083`。

### 低熱情度 vs 高熱情度

使用低熱情度問個簡單問題「15%的200是多少？」你會得到立即且直接的答案。現在使用高熱情度問個複雜的問題「設計一個適用於高流量 API 的快取策略」。點選 **🔴 串流回應（即時）**，觀看模型以逐字元的方式呈現詳盡推理。相同的模型，相同的問題結構—但提示決定它投入多少思考。

### 任務執行（工具前言）

多步驟工作流程適合事先計劃與過程解說。模型會先概述要做什麼，再敘述每一步驟，最後總結結果。

### 具自我反思的程式碼

試試「建立一個電子郵件驗證服務」。模型不只是生成程式碼然後停止，而是生成後自我評估品質標準，找出缺點並改進。你會看到它迭代直到程式碼達到生產標準。

### 結構化分析

程式碼審查需要一致的評估框架。模型根據固定類別分析程式碼（正確性、實踐、效能、安全性）並分級嚴重度。

### 多輪對話

問「什麼是 Spring Boot？」，接著緊接著問「給我一個範例」。模型會記住你第一個問題，並針對 Spring Boot 給你專門的範例。若沒有記憶，第二個問題會太籠統。

### 逐步推理

挑一道數學題，分別用逐步推理和低熱情度嘗試。低熱情度只給你答案—快速但不透明。逐步推理則會展示每個計算與決策步驟。

### 限制輸出

當你需要特定格式或字數時，此模式會嚴格遵守規範。試試用條列點產生一個100字的摘要。

## 你真正學到的是

**推理投入改變一切**

GPT-5.2 讓你能透過提示控制計算努力程度。低努力意味回應快速且探索有限。高努力表示模型花時間深入思考。你正在學習根據任務複雜度調整努力 - 不要在簡單問題上浪費時間，也不要匆忙決策複雜問題。

**結構引導行為**

注意提示中的 XML 標籤？它們不是裝飾。模型比起自由文本更可靠地跟隨結構化指令。當你需要多步驟流程或複雜邏輯時，結構能幫助模型追蹤位置和下一步。

<img src="../../../translated_images/zh-TW/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*一個結構良好的提示範例，清楚分區並採用 XML 風格組織*

**透過自我評估提升品質**

具自我反思的模式透過明確品質標準來運作。不再只是期待模型「做對」，你明示何謂「正確」：邏輯正確、錯誤處理、效能、安全。接著模型可以評估自身輸出並改進。這讓程式碼生成從彩票變成流程。

**上下文是有限的**

多輪對話透過每次請求包含訊息歷史實現。但有上限—每個模型都有最大 token 數。隨著對話增加，你需要策略來保持相關上下文而不觸頂。本模組展示記憶運作方式；未來你會學會何時摘要、何時遺忘、何時取回。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導覽：** [← 上一個：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一個：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們力求準確，但請注意，機器翻譯可能包含錯誤或不準確之處。原始文件的母語版本應為權威依據。對於重要資訊，建議委託專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->