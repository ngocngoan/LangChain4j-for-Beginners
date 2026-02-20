# Module 02: 使用 GPT-5.2 的提示工程

## 目錄

- [您將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [了解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零樣本提示](../../../02-prompt-engineering)
  - [少量範例提示](../../../02-prompt-engineering)
  - [思路鏈](../../../02-prompt-engineering)
  - [角色式提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有的 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索各種模式](../../../02-prompt-engineering)
  - [低與高熱忱](../../../02-prompt-engineering)
  - [任務執行（工具序言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [您真正學到的是什麼](../../../02-prompt-engineering)
- [後續步驟](../../../02-prompt-engineering)

## 您將學到什麼

<img src="../../../translated_images/zh-HK/what-youll-learn.c68269ac048503b2.webp" alt="您將學到什麼" width="800"/>

在上一個模組中，您了解了記憶如何使對話式 AI 成為可能，並使用 GitHub 模型進行基本互動。現在我們將專注於您如何提出問題——即提示本身——使用 Azure OpenAI 的 GPT-5.2。您撰寫提示的方式會極大影響回應的質量。我們從回顧基礎提示技巧開始，然後進入八個進階模式，充分利用 GPT-5.2 的功能。

我們選擇使用 GPT-5.2，是因為它引入了推理控制——您可以告訴模型在回答前要思考多少。這使得不同的提示策略更加明顯，並幫助您了解何時使用哪種方法。我們還將受益於 Azure 對 GPT-5.2 的較少速率限制，相較於 GitHub 模型。

## 先決條件

- 完成模組 01（已部署 Azure OpenAI 資源）
- 根目錄中有包含 Azure 憑證的 `.env` 文件（由模組 01 的 `azd up` 建立）

> **注意：** 如果尚未完成模組 01，請先按照該模組的部署說明操作。

## 了解提示工程

<img src="../../../translated_images/zh-HK/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文本，以持續提高您所需結果的技術。它不僅僅是問問題，而是結構化請求，使模型能準確理解您想要什麼以及如何交付。

把它想像成給同事下指示。「修正錯誤」很模糊。「修正 UserService.java 第 45 行的空指標異常，加入空值檢查」則非常具體。語言模型也是同理——具體與結構化相當重要。

<img src="../../../translated_images/zh-HK/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的定位" width="800"/>

LangChain4j 提供了基礎架構——模型連接、記憶和訊息類型——而提示模式只是通過這個基礎架構發送的精心結構化文本。主要組成部分是 `SystemMessage`（設定 AI 的行為和角色）和 `UserMessage`（承載您的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-HK/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式總覽" width="800"/>

在進入本模組的進階模式前，讓我們先回顧五個基礎的提示技巧。這些是每位提示工程師都應該知道的基石。如果您已經完成了[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，您應該已看過這些範例，這裡是背後的概念架構。

### 零樣本提示

最簡單的方式：給模型一條直接指令，沒有範例。模型完全依賴其訓練來理解和執行任務。對於行為明確的簡單請求效果很好。

<img src="../../../translated_images/zh-HK/zero-shot-prompting.7abc24228be84e6c.webp" alt="零樣本提示" width="800"/>

*不帶範例的直接指令——模型從指令中推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應：「正面」
```

**使用時機：** 簡單分類、直接提問、翻譯，或任何模型無需額外指引即可處理的任務。

### 少量範例提示

提供範例展示您想讓模型遵循的模式。模型從範例中學習期望的輸入輸出格式並應用於新輸入。這大大提升了格式或行為不明顯任務的一致性。

<img src="../../../translated_images/zh-HK/few-shot-prompting.9d9eace1da88989a.webp" alt="少量範例提示" width="800"/>

*從範例中學習——模型識別模式並應用新輸入*

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

**使用時機：** 客製化分類、一致格式化、特定領域任務，或零樣本結果不穩定時。

### 思路鏈

請模型逐步展現推理過程。模型不直接跳到答案，而是分解問題，逐部分地處理。這改善數學、邏輯和多步推理任務的準確度。

<img src="../../../translated_images/zh-HK/chain-of-thought.5cff6630e2657e2a.webp" alt="思路鏈提示" width="800"/>

*逐步推理——將複雜問題拆解成明確的邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**使用時機：** 數學問題、邏輯謎題、偵錯，或任何透過展示推理過程可以提升準確與信任的任務。

### 角色式提示

在提問前設定 AI 的角色或身份。這提供上下文，塑造回應的口吻、深度和焦點。像「軟體架構師」給出的建議與「初級開發者」或「安全稽核員」不同。

<img src="../../../translated_images/zh-HK/role-based-prompting.a806e1a73de6e3a4.webp" alt="角色式提示" width="800"/>

*設定上下文和身份—相同問題根據角色產生不同回應*

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

**使用時機：** 程式碼審查、教學、特定領域分析，或需針對特定專業層級或視角產出答案時。

### 提示模板

建立帶有變量佔位符的可重用提示。您不必每次都重寫提示，定義一次模板，再填入不同的值。LangChain4j 的 `PromptTemplate` 類用 `{{variable}}` 語法輕鬆實現。

<img src="../../../translated_images/zh-HK/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*可重用提示模板與變量占位符—一個模板，多種用法*

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

**使用時機：** 需要對不同輸入重複查詢、批次處理、建立可重複使用的 AI 工作流程, 或任何提示結構不變但資料不同的場景。

---

這五個基礎技巧為大多數提示任務提供堅實工具。這個模組的其餘部分建立在此基礎上，介紹八個利用 GPT-5.2 推理控制、自我評估和結構化輸出能力的**進階模式**。

## 進階模式

了解基礎後，我們進入讓本模組與眾不同的八個進階模式。不見得所有問題都需要相同策略。有些問題需要快速答案，有些則需深入思考。有些需要明顯的推理過程，有些只要結果。以下每個模式都為不同情境最佳化——而 GPT-5.2 的推理控制讓這些差異更明顯。

<img src="../../../translated_images/zh-HK/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示模式" width="800"/>

*八種提示工程模式及其使用情境總覽*

<img src="../../../translated_images/zh-HK/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 的推理控制讓您指定模型思考深度——從快速直接答覆到深入探索*

**低熱忱（快速且聚焦）** — 適用於您想要快速直接回覆的簡單問題。模型執行最少推理——最多兩步。用於計算、查詢或直接提問。

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
> - 「低熱忱與高熱忱的提示模式有何差異？」
> - 「提示中的 XML 標籤如何協助結構化 AI 的回應？」
> - 「何時應使用自我反思模式，何時應直接指令？」

**高熱忱（深入且徹底）** — 適用於您要全面分析的複雜問題。模型會深入探索，顯示詳細推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** — 適用於多步工作流程。模型提供事先規劃，逐步敘述各階段操作，最後給出總結。用於遷移、實作或任何多階段流程。

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

思路鏈提示明確要求模型示範推理過程，提升複雜任務的準確度。逐步拆解有助於人與 AI 理解邏輯。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 詢問此模式：
> - 「我該如何調整任務執行模式以應付長時間運作？」
> - 「在生產應用中，結構工具序言的最佳實務是什麼？」
> - 「我如何在介面中擷取並顯示中間進度更新？」

<img src="../../../translated_images/zh-HK/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結的多步工作流程*

**自我反思代碼** — 用於產生生產等級的代碼。模型依照生產標準產生包含適當錯誤處理的代碼。用於建立新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-HK/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改進循環—產生、評估、辨識問題、改善、重複*

**結構化分析** — 用於一致性評估。模型依固定框架審查程式碼（正確性、實務、效能、安全性、可維護性）。用於程式碼審查或品質評估。

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
> - 「如何為不同類型程式碼審查自訂分析框架？」
> - 「以程式化方式解析與使用結構化輸出的最佳方式？」
> - 「如何確保不同審查階段的一致嚴重度級別？」

<img src="../../../translated_images/zh-HK/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*一致程式碼審查框架及嚴重度等級*

**多輪對話** — 適用於需要上下文的對話。模型記憶先前訊息並據此建立。用於互動協助會話或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-HK/context-memory.dff30ad9fa78832a.webp" alt="對話上下文記憶" width="800"/>

*對話上下文如何隨多輪累積，直到達到 Token 限制*

**逐步推理** — 適用於需要明確邏輯的問題。模型展現每步的詳細推理。用於數學問題、邏輯謎題，或您想了解思考過程時。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-HK/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="逐步推理模式" width="800"/>

*將問題拆解成明確邏輯步驟*

**受限輸出** — 適用於需符合特定格式要求的回答。模型嚴格遵守格式與長度規則。用於摘要或您需精確輸出結構時。

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

<img src="../../../translated_images/zh-HK/constrained-output-pattern.0ce39a682a6795c2.webp" alt="受限輸出模式" width="800"/>

*強制特定格式、長度及結構要求*

## 使用現有的 Azure 資源

**驗證部署：**

確保根目錄存在含有 Azure 憑證的 `.env` 文件（模組 01 建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若您已在模組 01 使用 `./start-all.sh` 啟動所有應用，則本模組已運行於 8083 埠。您可跳過以下啟動指令，直接前往 http://localhost:8083。

**方案一：使用 Spring Boot Dashboard（建議 VS Code 使用者）**

開發容器內含 Spring Boot Dashboard 擴充功能，提供視覺化介面管理所有 Spring Boot 應用。可在 VS Code 左側活動欄找到（尋找 Spring Boot 圖標）。

通過 Spring Boot Dashboard，您可：
- 查看工作區中所有 Spring Boot 應用
- 一鍵啟動/停止應用
- 實時查看應用日誌
- 監控應用狀態
只需點擊「prompt-engineering」旁邊的播放按鈕即可開始此模組，或同時啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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

兩個腳本會自動從根目錄的 `.env` 文件載入環境變數，且如果 JAR 檔不存在會自動構建。

> **注意：** 如果你想在啟動前手動構建所有模組：
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
./stop.sh  # 僅此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只限此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-HK/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主控台展示了所有 8 種提示工程模式及其特點和使用案例*

## 探索這些模式

Web 介面讓你體驗不同的提示策略。每種模式解決不同問題——試試看，看看每種方法何時發揮最佳效果。

> **注意：串流與非串流** — 每個模式頁面提供兩個按鈕：**🔴 即時串流回應** 和 **非串流方式**。串流使用 Server-Sent Events (SSE) 即時顯示模型產生的每個 Token，因此你可以立即看到回應進度。非串流方式則會等整個回應完成後才顯示。對於需要深度推理的提示（例如高投入度、帶自我反思的程式碼），非串流呼叫可能會非常耗時，甚至長達數分鐘，且無法看到任何回饋。**實驗複雜提示時建議使用串流功能**，這樣你可以看到模型的工作過程，避免誤以為請求超時。
>
> **注意：瀏覽器需求** — 串流功能使用 Fetch Streams API (`response.body.getReader()`)，需全功能瀏覽器支援（如 Chrome、Edge、Firefox、Safari）。在 VS Code 內建的 Simple Browser 中無法使用，因其 Webview 不支援 ReadableStream API。使用 Simple Browser 時，非串流按鈕仍然正常，只有串流按鈕不可用。請使用外部瀏覽器開啟 `http://localhost:8083` 以獲得完整體驗。

### 低投入度 vs 高投入度

用低投入度問一個簡單問題，例如「200 的 15% 是多少？」你會立即得到直接答案。現在用高投入度問一個複雜問題，例如「設計一個高流量 API 的快取策略」。點擊 **🔴 即時串流回應**，觀看模型逐字生成詳細推理。使用的是同一模型、同一問題結構，但提示決定了模型思考的深度。

### 任務執行 (工具序言)

多步驟工作流程需要事先計劃與過程敘述。模型會先說明將要執行的事，然後逐步敘述，最後總結結果。

### 自我反思程式碼

試試「建立一個電子郵件驗證服務」。模型不只是產生程式碼然後停下，會先產生、根據品質標準評估、找出缺點，接著改進。你會看到它持續迭代直到程式碼符合生產標準為止。

### 結構化分析

程式碼審查需要一致的評估框架。模型會依固定分類（正確性、實踐、效能、安全性）與嚴重程度來分析程式碼。

### 多回合對話

提問「什麼是 Spring Boot？」然後緊接著問「示範一個範例」。模型會記住你的第一個問題，並特別給你一個 Spring Boot 範例。若沒有記憶，第二個問題就過於模糊。

### 逐步推理

挑一個數學題，用逐步推理和低投入度兩種方式試試看。低投入度直接給答案——快速但不透明。逐步推理則顯示每個計算與判斷過程。

### 限制輸出

當你需要特定格式或字數時，這種模式會嚴格執行要求。試試用條列格式產生精確 100 字的摘要。

## 你真正學到的是什麼

**推理努力改變一切**

GPT-5.2 讓你透過提示控制計算努力程度。低努力意味著快速回應與最少探索；高努力表示模型會花時間深入思考。你學會根據任務複雜度匹配努力程度——不要浪費時間在簡單問題，也別急於做複雜決策。

**結構指導行為**

注意提示中的 XML 標籤？它們不是裝飾。模型比起自由文本，更準確地跟隨具結構的指令。需要多步驟流程或複雜邏輯時，結構能幫助模型追蹤現階段與後續步驟。

<img src="../../../translated_images/zh-HK/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*一個結構良好的提示示例，具有清晰分段和 XML 風格組織*

**透過自我評估確保品質**

自我反思模式的運作在於明確品質標準。不再寄望模型「做對」，你明確告訴它「對」的標準是什麼：邏輯正確、錯誤處理、效能與安全。模型因此可以自我評估並改進輸出，讓程式碼產生變成一個有規律的過程，而非運氣遊戲。

**上下文有限**

多回合會話是透過每次請求帶入訊息歷史實現，但每個模型有最大 token 數限制。隨著對話增長，你需要策略管理關鍵上下文，避免超出限制。本模組展示記憶如何運作；日後你會學習什麼時候摘要、什麼時候忘記、什麼時候取回訊息。

## 下一步

**下一模組：** [03-rag - RAG (檢索增強生成)](../03-rag/README.md)

---

**導覽：** [← 上一章：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一章：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於重要資訊，建議使用專業人員翻譯。我們對因使用本翻譯而引起的任何誤解或誤譯概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->