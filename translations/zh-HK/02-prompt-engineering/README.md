# Module 02: 使用 GPT-5.2 的提示工程

## 目錄

- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少次提示](../../../02-prompt-engineering)
  - [思維鏈](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低與高渴望](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你將學到什麼

<img src="../../../translated_images/zh-HK/what-youll-learn.c68269ac048503b2.webp" alt="你將學到什麼" width="800"/>

在上一模組中，您看到記憶如何讓會話 AI 運作並使用 GitHub 模型進行基本互動。現在我們將專注於您如何提問 — 提示本身 — 使用 Azure OpenAI 的 GPT-5.2。您構造提示的方式會大幅影響您收到的回應品質。我們首先回顧基本的提示技術，然後進入八個進階模式，充分利用 GPT-5.2 的能力。

我們使用 GPT-5.2，是因為它引入了推理控制 — 您可以告訴模型在回答前該思考多少。這讓不同的提示策略變得更明顯，並幫助您了解何時使用哪種方法。我們也會受惠於 Azure 對 GPT-5.2 較少的速率限制，比起 GitHub 模型更有優勢。

## 先決條件

- 完成模組 01（已部署 Azure OpenAI 資源）
- 根目錄下含 `.env` 文件，內含 Azure 認證（由模組 01 中 `azd up` 創建）

> **注意：** 如果尚未完成模組 01，請先跟隨那裡的部署說明。

## 理解提示工程

<img src="../../../translated_images/zh-HK/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文字，讓你穩定獲得想要的結果。它不只是提出問題 — 還是結構化請求，讓模型準確理解你需要什麼，該如何回應。

想像你是在對同事發出指示。「修復錯誤」很模糊；「在 UserService.java 第 45 行加上空指標檢查修復 null pointer exception」很具體。語言模型也是這樣 — 具體性與結構很重要。

<img src="../../../translated_images/zh-HK/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的角色" width="800"/>

LangChain4j 提供基礎設施 — 模型連接、記憶和訊息類型 — 而提示模式只是你通過該基礎設施傳送的精心結構化文本。關鍵構建塊是 `SystemMessage`（設定 AI 行為與角色）和 `UserMessage`（傳遞你的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-HK/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式概覽" width="800"/>

在深入本模組的進階模式前，讓我們回顧五種基礎提示技術。這些是每位提示工程師都應知道的構建基礎。若您已完成[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，您已看到它們的實例 — 下面是其背後的概念框架。

### 零次提示

最簡單的方法：直接給模型指示，無需範例。模型完全依賴訓練來理解和執行任務。適合簡單直觀的請求。

<img src="../../../translated_images/zh-HK/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次提示" width="800"/>

*無範例的直接指令 — 模型從指令中推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應：「正面」
```

**適用時機：** 簡單分類、直接問答、翻譯或任何模型能不需額外指引處理的任務。

### 少次提示

提供範例示範你希望模型遵循的模式。模型從範例學習預期的輸入輸出格式，並將其應用於新輸入。大幅提升格式或行為不明顯任務的一致性。

<img src="../../../translated_images/zh-HK/few-shot-prompting.9d9eace1da88989a.webp" alt="少次提示" width="800"/>

*從範例學習 — 模型辨識模式並應用於新輸入*

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

**適用時機：** 自訂分類、一致格式化、特定領域任務，或零次結果不穩定時。

### 思維鏈

要求模型逐步展現推理過程。模型不急於直接回答，而是分解問題並清楚地處理每一步。提升數學、邏輯與多步推理任務的準確性。

<img src="../../../translated_images/zh-HK/chain-of-thought.5cff6630e2657e2a.webp" alt="思維鏈提示" width="800"/>

*逐步推理 — 將複雜問題拆解成明確的邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**適用時機：** 數學題、邏輯謎題、除錯，或任何展示推理過程可提升準確度與信任的任務。

### 基於角色的提示

在提問前設定 AI 的身份或角色。這提供上下文，決定回應的語氣、深度與重點。「軟件架構師」會提供與「初級開發者」或「安全審核員」不同的建議。

<img src="../../../translated_images/zh-HK/role-based-prompting.a806e1a73de6e3a4.webp" alt="基於角色的提示" width="800"/>

*設定上下文與身份 — 同一問題因分配角色不同，得到不同回覆*

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

**適用時機：** 程式碼審查、教學、領域專家分析，或需要依特定專業層級或視角客製化回應時。

### 提示模板

創建可重複使用的帶變數佔位符提示。無需每次撰寫新提示，一次定義模板，填入不同值。LangChain4j 的 `PromptTemplate` 類支援 `{{variable}}` 語法輕鬆達成。

<img src="../../../translated_images/zh-HK/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*帶變數佔位符的可重用提示 — 一個模板，多種用途*

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

**適用時機：** 重複查詢不同輸入、批次處理、建立可重用 AI 工作流，或任何提示結構相同僅內容變化的場景。

---

這五項基礎技術為大部分提示任務提供堅實工具。本模組其他內容將在此基礎上，介紹利用 GPT-5.2 推理控制、自我評估與結構化輸出能力的 **八種進階模式**。

## 進階模式

基礎技能介紹完後，接著介紹本模組的八種進階模式。並非所有問題都用同一方法。有些問題需要快速回答，有些則需要深入思考。有些需要展示推理過程，有些只要結果。以下模式依場景優化 — GPT-5.2 推理控制使這些差異更明顯。

<img src="../../../translated_images/zh-HK/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示工程模式" width="800"/>

*八種提示工程模式及其使用案例概覽*

<img src="../../../translated_images/zh-HK/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 推理控制允許你指定模型要思考多少 — 從快速直接到深入探索皆可*

**低渴望（快速且聚焦）** - 適用簡單問題，想快速直接得答案。模型推理步驟最少，最多 2 步。適合計算、查詢或簡單問題。

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

> 💡 **使用 GitHub Copilot 探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 並提問：
> - 「低渴望和高渴望提示模式有什麼區別？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「什麼時候應使用自我反思模式而非直接指令？」

**高渴望（深度且周全）** - 適用複雜問題，想要完整分析。模型仔細探索並展示詳盡推理。適合系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** - 適用多步工作流。模型先提供計劃，執行時口述每步，最後做總結。用於遷移、實作或任何多步驟過程。

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

思維鏈提示明確要求模型展示推理過程，提升複雜任務的準確性。逐步分解有助人與 AI 理解邏輯。

> **🤖 試試 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問此模式：
> - 「如何調整任務執行模式以應對長時間操作？」
> - 「生產應用中結構工具前言的最佳實踐是什麼？」
> - 「如何在 UI 中擷取並呈現中間進度更新？」

<img src="../../../translated_images/zh-HK/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結的多步任務工作流*

**自我反思代碼** - 生成生產級代碼。模型依生產標準生成代碼並妥善處理錯誤。適用於打造新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-HK/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思迴圈" width="800"/>

*迭代改進循環 — 生成、評估、識別問題、改進，重複*

**結構化分析** - 用於一致性評估。模型用固定框架（正確性、實踐、效能、安全、維護性）審查代碼。適用程式碼審查或品質評估。

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

> **🤖 試試 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問結構化分析：
> - 「如何為不同類型的代碼審查自訂分析框架？」
> - 「以程式方式解析並對結構化輸出採取行動的最佳方法？」
> - 「如何確保不同審查階段一致的嚴重性級別？」

<img src="../../../translated_images/zh-HK/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*用於一致程式碼審查的框架及嚴重性分級*

**多輪對話** - 適用需上下文的對話。模型記住先前訊息並基於此擴展。適合互動式幫助或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-HK/context-memory.dff30ad9fa78832a.webp" alt="上下文記憶" width="800"/>

*會話上下文在多輪中累積，直到達到令牌限制*

**逐步推理** - 適用需可視邏輯的問題。模型展示每步明確推理。用於數學題、邏輯謎題，或需要理解思考過程時。

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

*將問題拆解為明確邏輯步驟*

**受限輸出** - 適用有特定格式需求的回應。模型嚴格遵循格式與長度規則。用於摘要或需精確輸出結構時。

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

*強制特定格式、長度及結構需求*

## 使用現有 Azure 資源

**確認部署：**

確保根目錄有含 Azure 認證的 `.env` 文件（模組 01 過程中建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用：**

> **注意：** 若您已在模組 01 使用 `./start-all.sh` 啟動所有應用，本模組已運行於 8083 埠，可跳過以下啟動操作，直接訪問 http://localhost:8083。

**選項 1：使用 Spring Boot 儀表板（建議 VS Code 用戶）**

開發容器已包含 Spring Boot 儀表板擴充，提供視覺介面管理所有 Spring Boot 應用。可在 VS Code 左側活動欄找到（尋找 Spring Boot 圖示）。

在 Spring Boot 儀表板中，您可以：
- 查看工作區中所有可用的 Spring Boot 應用
- 單擊啟動/停止應用
- 即時查看應用日誌
- 監控應用狀態
只需按一下「prompt-engineering」旁邊的播放按鈕即可啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有 Web 應用程式（模組 01-04）：

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

兩個腳本會自動從根目錄的 `.env` 檔案讀取環境變數，並在 JAR 不存在時自動建立。

> **注意：** 如果你想要在啟動前手動建置所有模組：
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

**停止方法：**

**Bash:**
```bash
./stop.sh  # 只有此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-HK/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主控制台展示所有 8 種提示工程模式及其特徵和使用案例*

## 探索這些模式

Web 介面讓你嘗試不同的提示策略。每種模式解決不同問題──試試看，了解每種方法何時最有效。

### 低認真度與高認真度

用低認真度問一個簡單問題：「15% 的 200 是多少？」你會獲得即時直接的答案。接著用高認真度問一個複雜問題：「設計一個針對高流量 API 的快取策略」。你會看到模型放慢速度，提供詳細推理。相同模型、相同問題結構──只是提示告訴它需要多少思考。

<img src="../../../translated_images/zh-HK/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*快速計算，推理最小化*

<img src="../../../translated_images/zh-HK/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*完整的快取策略（2.8MB）*

### 任務執行（工具前導）

多步驟工作流程需要提前規劃並說明進度。模型會先概述將執行的內容，逐步說明每一階段，最後總結結果。

<img src="../../../translated_images/zh-HK/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*逐步說明如何建立 REST 端點（3.9MB）*

### 自我反思程式碼

試著問「建立一個電子郵件驗證服務」。模型不會停止於單純產生程式碼，而是會生成、根據品質標準評估、辨識問題並改進。你會看到它反覆迭代直到符合生產標準。

<img src="../../../translated_images/zh-HK/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*完整的電子郵件驗證服務（5.2MB）*

### 結構化分析

程式碼審查需要一致的評估架構。模型會用固定類別（正確性、實務、效能、安全性）結合嚴重級別來分析程式碼。

<img src="../../../translated_images/zh-HK/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*基於架構的程式碼審查*

### 多輪對話

問「什麼是 Spring Boot？」然後馬上追問「給我一個範例」。模型會記住你的第一個問題，並專門給你一個 Spring Boot 範例。沒記憶的話，第二個問題會太模糊。

<img src="../../../translated_images/zh-HK/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*跨問題保留上下文*

### 逐步推理

選一個數學問題，分別用逐步推理和低認真度嘗試。低認真度只直接給答案──快速但不透明。逐步推理會展示所有計算和決策步驟。

<img src="../../../translated_images/zh-HK/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*明確步驟的數學問題*

### 限制輸出

當你需要特定格式或字數時，此模式會嚴格遵守規定。試著生成一篇正好 100 個字的重點摘要。

<img src="../../../translated_images/zh-HK/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*機器學習摘要，附格式管控*

## 你真正學到的是什麼

**推理努力改變一切**

GPT-5.2 讓你透過提示控制運算努力。低努力代表快速回應，並少做探索。高努力代表模型花時間深度思考。你學會了根據任務複雜度調整努力──不浪費時間於簡單問題，也不輕率處理複雜決策。

**結構引導行為**

注意提示中的 XML 標籤嗎？它們不是裝飾。模型比起自由格式文字，更可靠地遵循結構化指示。需要多步驟或複雜邏輯時，結構能幫助模型追蹤當前狀態與後續流程。

<img src="../../../translated_images/zh-HK/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*結構良好提示的構造，清晰分節與 XML 風格組織*

**品質來自自我評估**

自我反思模式藉由明確品質標準運作。你不必靠運氣讓模型「做對」，而是明確告訴它什麼是「正確」：邏輯正確、錯誤處理、效能、安全。模型能自我評估並改進。這使程式碼生成從中獎變成穩定流程。

**上下文是有限的**

多輪對話透過每次請求帶入訊息歷史實現。但有上限──每個模型都有最大token數。對話愈長，你需要策略保留重要上下文，避免超出上限。本模組演示記憶如何運作；之後你會學習何時摘要、何時忘記、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導覽：** [← 上一個：模組 01 - 介紹](../01-introduction/README.md) | [返回主頁](../README.md) | [下一個：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的原文版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->