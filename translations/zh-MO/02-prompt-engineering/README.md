# Module 02: 使用 GPT-5.2 的提示工程

## 目錄

- [視頻導覽](../../../02-prompt-engineering)
- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零樣本提示](../../../02-prompt-engineering)
  - [少樣本提示](../../../02-prompt-engineering)
  - [思維鏈](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低與高積極性](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [約束輸出](../../../02-prompt-engineering)
- [你真正學習的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 視頻導覽

觀看這個直播課程，了解如何開始這個模組：

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## 你將學到什麼

<img src="../../../translated_images/zh-MO/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

在上一個模組中，你已了解記憶是如何使對話式 AI 運作的，並使用 GitHub 模型來進行基本互動。現在我們將專注於你如何提出問題──即提示本身──利用 Azure OpenAI 的 GPT-5.2。你如何構造提示，會大幅影響你得到的回應品質。我們會先回顧基本提示技巧，然後介紹八種進階模式，充分利用 GPT-5.2 的能力。

我們使用 GPT-5.2 是因為它引入了推理控制──你可以告訴模型在回答前需要進行多少思考。這讓不同的提示策略更加明顯，幫助你理解何時使用各種策略。相較於 GitHub 模型，我們也可以從 Azure 上 GPT-5.2 較少的速率限制中獲益。

## 先決條件

- 完成本模組前置的 Module 01（已部署 Azure OpenAI 資源）
- 根目錄下 `.env` 文件包含 Azure 憑證（由 Module 01 的 `azd up` 指令建立）

> **注意：** 若未完成 Module 01，請先依那裡的部署指示操作。

## 理解提示工程

<img src="../../../translated_images/zh-MO/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

提示工程是設計輸入文字，讓你穩定地獲得所需結果。它不只是提問──還包括結構化請求，讓模型準確理解你想要什麼，並如何回應。

可以想像成給同事指示。「修正錯誤」很模糊。「修正 UserService.java 第 45 行的 null pointer 例外，通過加入 null 檢查」就很明確。語言模型也是如此──具體又有結構很重要。

<img src="../../../translated_images/zh-MO/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j 提供架構──模型連結、記憶與訊息類型──而提示模式即是在該架構下小心設計的文字。關鍵組件是 `SystemMessage`（設置 AI 的行為和角色）與 `UserMessage`（承載你的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-MO/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

在深入本模組的進階模式前，先回顧五個基礎提示技巧。這些是每個提示工程師必知的基石。如果你已經看過 [快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，應該見過這些運用─以下是其概念框架。

### 零樣本提示

最簡單的方式：給模型一個直接指令，不提供範例。模型完全仰賴訓練內容理解並執行任務。適用於預期行為明確的簡單請求。

<img src="../../../translated_images/zh-MO/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*無範例的直接指令──模型僅依指令推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應:「正面」
```

**適用時機：** 簡單分類、直接問題、翻譯或模型能無額外指導下處理的任務。

### 少樣本提示

提供範例示範你希望模型遵循的模式。模型從範例中學習預期的輸入輸出格式，並套用到新輸入上。這顯著增強格式或行為不明顯任務的一致性。

<img src="../../../translated_images/zh-MO/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*從範例學習──模型識別模式並應用於新輸入*

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

**適用時機：** 客製分類、一致格式、特定領域任務或零樣本結果不穩定時。

### 思維鏈

讓模型展示逐步推理過程。模型非直接跳到答案，而是分解問題、一點一點解決。提升數學、邏輯與多步推理任務的準確度。

<img src="../../../translated_images/zh-MO/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*逐步推理──將複雜問題拆解成明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**適用時機：** 數學題、邏輯謎題、除錯或需要展示推理過程提升準確度和信任度的任務。

### 基於角色的提示

在提問前設定 AI 的角色或身份。這提供上下文，影響回答的語氣、深度和重點。「軟體架構師」會給出不同建議於「初級開發者」或「安全審計師」。

<img src="../../../translated_images/zh-MO/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*設定上下文與角色──同一問題在不同角色下答覆會不同*

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

**適用時機：** 程式碼審查、教學、特定領域分析或需要依專業程度／觀點給出量身定制回應時。

### 提示模板

建立帶變數佔位符的可重複使用提示。無須每回都寫新提示，只要定義一個模板，填入不同數值。LangChain4j 的 `PromptTemplate` 類使用 `{{variable}}` 語法使此事簡單。

<img src="../../../translated_images/zh-MO/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*帶變數佔位符的可重用提示──一個模板，多種用途*

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

**適用時機：** 多次查詢不同輸入、批量處理、構建可重用的 AI 工作流程，或任何提示結構固定但資料變動的情形。

---

這五項基礎技能為你提供了大部分提示任務的堅實工具包。本模組剩餘部分則建立在此，介紹**八種進階模式**，善用 GPT-5.2 的推理控制、自我評估與結構化輸出的能力。

## 進階模式

基礎內容介紹完畢，接著介紹讓本模組獨特的八種進階模式。並非所有問題都需同種方式，有些問題需快速回答，有些需深入思考。有些需可見推理，有些只要結果。下列每種模式皆針對不同場景優化——而 GPT-5.2 的推理控制更使差異明顯。

<img src="../../../translated_images/zh-MO/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八種提示工程模式及其應用簡介*

<img src="../../../translated_images/zh-MO/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 提供推理控制，讓你指定模型思考深度──從快速直接答覆到深入探索*

**低積極性（快速且專注）** - 對簡單問題需快速直接回應時。模型推理步驟極少──最多兩步。用於計算、查詢或直接問題。

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

> 💡 **利用 GitHub Copilot 探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 並詢問：
> - 「低積極性和高積極性提示模式的差異是什麼？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「何時該用自我反思模式，何時用直接指示？」

**高積極性（深入且全面）** - 對複雜問題需全面分析時。模型徹底探索並展示詳細推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** - 適用多步驟工作流程。模型先提供計畫、執行過程邊敘述，最後總結。用於遷移、實作或任何多步流程。

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

思維鏈提示清楚要求模型展示推理過程，提升複雜任務準確度。逐步拆解有助於人與 AI 理解邏輯。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能試試看：** 詢問這模式：
> - 「如何調整任務執行模式處理長時間運行操作？」
> - 「生產環境中結構工具前言的最佳作法是？」
> - 「如何在介面中擷取並顯示中途進度更新？」

<img src="../../../translated_images/zh-MO/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*計畫 → 執行 → 總結 多步任務工作流程*

**自我反思代碼** - 產出符合生產標準的程式碼。模型遵循生產準則，含適當錯誤處理。用於構建新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-MO/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*迭代改進循環──產生、評估、找出問題、改進、重複*

**結構化分析** - 進行一致性評價。模型使用固定架構審查程式碼（正確性、實務、效能、安全、維護）。適用程式碼審查或品質評估。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能試試看：** 詢問結構化分析：
> - 「如何針對不同類型程式碼審查自訂分析框架？」
> - 「以程式方式解析及處理結構化輸出的最佳方法？」
> - 「如何確保不同審查會話間嚴重性等級一致？」

<img src="../../../translated_images/zh-MO/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*用嚴重性等級建立一致程式碼審查框架*

**多輪聊天** - 適合需要上下文的對話。模型記憶前面訊息並在此基礎上回覆。用於互動式輔助或複雜問答。

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

*對話上下文如何隨多輪積累直到達到 Token 上限*

**逐步推理** - 需要可見邏輯的問題。模型展示每步明確推理。適用數學題、邏輯謎題或需理解推理過程時。

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

*將問題拆解為明確的邏輯步驟*

**約束輸出** - 回應需特定格式限制時。模型嚴格遵守格式及長度規範。用於摘要或需精確輸出結構場合。

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

*強制特定格式、長度及結構要求*

## 使用現有 Azure 資源

**驗證部署：**

確保根目錄有 `.env` 文件且內含 Azure 憑證（由 Module 01 建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已用 Module 01 的 `./start-all.sh` 指令啟動所有應用程式，本模組已在 8083 埠運行。你可以略過以下啟動命令，直接訪問 http://localhost:8083。
**選項 1：使用 Spring Boot 儀表板（建議 VS Code 使用者）**

開發容器包含 Spring Boot 儀表板擴充套件，提供一個視覺化介面來管理所有 Spring Boot 應用程式。你可以在 VS Code 左側的活動列中找到它（尋找 Spring Boot 圖示）。

從 Spring Boot 儀表板，你可以：
- 查看工作區中所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 即時檢視應用程式日誌
- 監控應用程式狀態

只需按一下 "prompt-engineering" 旁邊的播放按鈕即可啟動此模組，或同時啟動所有模組。

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
cd ..  # 從根目錄
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

兩個腳本會自動從根目錄的 `.env` 檔案載入環境變數，並且如果 JAR 檔案不存在，會進行建置。

> **注意：** 若你想在啟動之前手動建置所有模組：
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

**停止方式：**

**Bash:**
```bash
./stop.sh  # 僅此模組
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

<img src="../../../translated_images/zh-MO/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主儀表板顯示所有 8 種 prompt 工程模式及其特性和使用案例*

## 探索各種模式

網頁介面讓你試驗不同的提示策略。每種模式解決不同的問題 — 嘗試它們，看看每種方法何時發揮效果。

> **注意：串流與非串流** — 每個模式頁面提供兩個按鈕：**🔴 串流回應（即時）** 及 **非串流** 選項。串流採用 Server-Sent Events (SSE)，在模型生成時即刻顯示 token，令你能即時看到進度。非串流選項則是在整個回應完成後才顯示。對於需要深入推理的提示（例如 高熱忱、反思式程式碼），非串流呼叫可能需要非常長時間 — 可能長達數分鐘 — 期間沒有任何回饋。**在試驗複雜提示時請使用串流功能**，以便觀看模型的運作過程，避免誤以為請求已超時。
>
> **注意：瀏覽器需求** — 串流功能使用 Fetch Streams API (`response.body.getReader()`)，需要完整的瀏覽器（Chrome、Edge、Firefox、Safari）。在 VS Code 內建的簡易瀏覽器無法使用，因其 Webview 不支援 ReadableStream API。如使用簡易瀏覽器，非串流按鈕依然正常，但串流按鈕則無法使用。請在外部瀏覽器中開啟 `http://localhost:8083`，以獲得完整體驗。

### 低熱忱 vs 高熱忱

用低熱忱問簡單問題如「15% 的 200 是多少？」會迅速獲得直接回答。用高熱忱問複雜問題如「設計高流量 API 的快取策略」，點選 **🔴 串流回應（即時）**，觀察模型逐字生成詳細的推理。相同模型，相同問題結構 — 卻告訴模型需投入多少心力思考。

### 任務執行（工具前言）

多步驟工作流程受益於事先規劃與進度敘述。模型會先大致說明待執行事項，敘述每個步驟，最後總結結果。

### 反思式程式碼

試試「建立郵件驗證服務」。模型不只是產生程式碼後停下，還會產生後依品質標準評估、找出弱點並改進。你將看到它反覆修改直到程式碼符合生產標準。

### 結構化分析

程式碼審查需要一致的評估架構。模型透過固定分類（正確性、實務、效能、安全性）與嚴重程度進行分析。

### 多輪對話

先問「什麼是 Spring Boot？」接著緊接著問「給我一個範例」。模型記得你的第一個問題，會針對 Spring Boot 提供具體範例。若無記憶，第二個問題就太寬泛。

### 逐步推理

挑一題數學問題，分別用逐步推理與低熱忱嘗試。低熱忱僅快狠準給出答案，但不透明。逐步推理則展示每個計算與決策。

### 受限輸出

需要特定格式或字數時，這個模式嚴格遵守規範。試著用 100 字、條列式形式產生摘要。

## 你真正學到的是什麼

**推理深度改變一切**

GPT-5.2 讓你透過提示控制計算努力。低努力意味快速回應且探索有限。高努力意味模型花時間深入思考。你在學習如何依問題複雜度調整努力 — 不要在簡單問題浪費功夫，也別匆忙下複雜決策。

**結構引導行為**

注意提示中的 XML 標籤嗎？它們並非裝飾。模型比起自由文本，更能可靠遵從結構化指令。當需要多步驟過程或複雜邏輯時，結構方便模型追蹤狀態與下一步。

<img src="../../../translated_images/zh-MO/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*結構良好的提示解析，包含清晰區段與類 XML 組織*

**透過自我評估提升品質**

反思式模式透過清楚列出品質標準運作。不再只是期待模型「正確執行」，而是明確告訴它「正確」是什麼：邏輯正確、錯誤處理、效能及安全性。模型能自我評估輸出並改進。這讓程式碼生成不再是抽獎，而是可控流程。

**上下文有限**

多輪對話藉由於每次請求包含訊息歷史維持上下文。但有上限 — 每個模型允許的最大 token 數。對話越長，你需策略性保留相關上下文，避免超過限制。本模組示範記憶如何運作；之後你會學到何時摘要、何時遺忘、何時提取。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導覽：** [← 上一章：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一章：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始語言版本的文件應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->