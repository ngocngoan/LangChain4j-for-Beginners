# Module 02: 使用 GPT-5.2 進行提示工程

## 目錄

- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少量示例提示](../../../02-prompt-engineering)
  - [思路鏈](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低與高渴望度](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你將學到什麼

<img src="../../../translated_images/zh-MO/what-youll-learn.c68269ac048503b2.webp" alt="你將學到什麼" width="800"/>

在上一模組中，你已看到記憶如何支持對話式 AI，並使用 GitHub 模型進行基礎交互。現在我們將專注於如何提問 —— 即提示本身 —— 使用 Azure OpenAI 的 GPT-5.2。提示的結構方式會大幅影響你得到的回答質量。我們先回顧基本的提示技術，然後進入八種充分利用 GPT-5.2 功能的進階模式。

我們使用 GPT-5.2 是因為它引入了推理控制 —— 你可以告訴模型回答前要進行多少思考。這使不同的提示策略更為明顯，也幫助你理解何時使用每種方法。此外，與 GitHub 模型相比，Azure 對 GPT-5.2 的速率限制更少，讓我們受益良多。

## 先決條件

- 完成模組 01（已佈署 Azure OpenAI 資源）
- 專案根目錄有 `.env` 檔案，內含 Azure 認證（由模組 01 的 `azd up` 建立）

> **注意：** 如果你尚未完成模組 01，請先依照那裡的部署說明操作。

## 理解提示工程

<img src="../../../translated_images/zh-MO/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文本，使你能持續得到所需結果的過程。這不只是提問，而是結構化請求，使模型明確理解你想要什麼以及怎樣提供。

可以把它想像成給同事指示。「修復錯誤」就很模糊。「在 UserService.java 的第 45 行透過加入空指標檢查修復空指標異常」就很具體。語言模型的工作原理也是如此 —— 具體性與結構很重要。

<img src="../../../translated_images/zh-MO/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的作用" width="800"/>

LangChain4j 提供基礎設施——模型連接、記憶和訊息型別——而提示模式則是你透過該基礎設施發送的經過精心結構化的文本。關鍵構建區塊是 `SystemMessage`（設定 AI 的行為和角色）與 `UserMessage`（承載你的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-MO/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式總覽" width="800"/>

在深入本模組的進階模式前，先回顧五種基礎提示技術。這些是每位提示工程師應該知道的基石。如果你已經看過[快速啟動模組](../00-quick-start/README.md#2-prompt-patterns)，你會見過這些實例 —— 這裡是它們背後的概念框架。

### 零次提示

最簡單的方法：直接給模型指令，無需範例。模型完全依靠訓練理解並執行任務。這適合預期行為明確的簡單請求。

<img src="../../../translated_images/zh-MO/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次提示" width="800"/>

*無範例直接指令 —— 模型僅從指令推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應：「正面」
```

**什麼時候用：** 簡易分類、直接提問、翻譯或任何模型能無須額外指引處理的任務。

### 少量示例提示

提供示例展示你期望模型遵循的模式。模型從示例學習輸入輸出格式，套用於新輸入。對於期望格式或行為不明顯的任務，這大幅提升一致性。

<img src="../../../translated_images/zh-MO/few-shot-prompting.9d9eace1da88989a.webp" alt="少量示例提示" width="800"/>

*從示例學習 —— 模型識別模式並套用於新輸入*

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

**什麼時候用：** 自訂分類、一致格式化、特定領域任務，或零次結果不穩定時。

### 思路鏈

請模型呈現逐步推理過程。模型不跳過直接回答，而是分解問題，各部分明確演算。提升數學、邏輯、與多步推理的準確度。

<img src="../../../translated_images/zh-MO/chain-of-thought.5cff6630e2657e2a.webp" alt="思路鏈提示" width="800"/>

*逐步推理 —— 將複雜問題拆解為明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**什麼時候用：** 數學問題、邏輯謎題、除錯，或任何顯示推理過程能提升準確性與信任的任務。

### 基於角色的提示

在提問前設定 AI 的角色或身份。這提供內容脈絡，決定回答的口吻、深度與重點。「軟件架構師」提供的建議不同於「初級開發者」或「安全審計員」。

<img src="../../../translated_images/zh-MO/role-based-prompting.a806e1a73de6e3a4.webp" alt="基於角色的提示" width="800"/>

*設置脈絡與角色 —— 同一問題因角色不同得到不同回應*

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

**什麼時候用：** 程式碼審查、輔導、特定領域分析，或需要特定專業層級或觀點的回答時。

### 提示模板

創建可重用的提示，包含可變佔位符。非每次皆寫新提示，定義一次模板反覆填入不同值。LangChain4j 的 `PromptTemplate` 類提供 `{{variable}}` 語法輕鬆實現。

<img src="../../../translated_images/zh-MO/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*可重用提示含變量佔位符 —— 一個模板，多種用法*

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

**什麼時候用：** 重複查詢不同輸入、批量處理、構建可重用 AI 工作流程，或提示結構一致但資料變化時。

---

這五項基礎為大多數提示任務提供堅實工具箱。本模組餘下部分基於此，介紹 **八種進階模式**，利用 GPT-5.2 的推理控制、自我評估與結構化輸出能力。

## 進階模式

基礎已講完，接下來介紹本模組獨特的八種進階模式。並非所有問題都用同一方案。有些問題需快速直答，有些則需深度思考。有些需要展示明晰推理，有些只需結果。以下每種模式針對不同場景優化 —— GPT-5.2 的推理控制令這些差異更為顯著。

<img src="../../../translated_images/zh-MO/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示模式" width="800"/>

*八種提示工程模式及其使用情境總覽*

<img src="../../../translated_images/zh-MO/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 推理控制" width="800"/>

*GPT-5.2 推理控制功能讓你指定模型應思考多少 —— 從快速直接回答到深入探索*

**低渴望度（快速且聚焦）** - 適用簡單問題，需快速直接回答。模型推理最少 —— 最多兩步。用於計算、查詢或直接問題。

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

> 💡 **使用 GitHub Copilot 探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 詢問：
> - 「低渴望度與高渴望度提示模式有何差異？」
> - 「提示中的 XML 標籤如何輔助結構化 AI 回應？」
> - 「何時應使用自我反思模式，何時用直接指令？」

**高渴望度（深度且徹底）** - 適用複雜問題，需全面分析。模型展開深入探索，展示詳細推理。用於系統設計、架構決策、複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進度）** - 多步工作流程模式。模型先給出計劃，執行時說明每步，最後總結。用於遷移、實作或任意多步程序。

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

思路鏈提示明確要求模型展示推理過程，提升複雜任務正確率。逐步拆解助人與 AI 理解邏輯。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 詢問此模式：
> - 「如何調整任務執行模式以支援長時間運行？」
> - 「生產環境中工具前言的結構最佳實踐是什麼？」
> - 「如何在 UI 中捕獲並顯示中間進度更新？」

<img src="../../../translated_images/zh-MO/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結的多步任務流程*

**自我反思代碼** - 產出符合生產標準的程式碼。模型生成具錯誤處理的生產品質碼。用於構建新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-MO/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改進循環 —— 生成、評估、發現問題、改進、重複*

**結構化分析** - 用固定框架進行一致性評估。模型依正確性、實踐、效能、安全、可維護性檢視程式碼。適用程式碼審查或品質評估。

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
> - 「如何為不同程式碼審查類型自訂分析框架？」
> - 「從結構化輸出程式化解析及處理的最佳方法是什麼？」
> - 「如何確保不同審查場次的一致嚴重度標準？」

<img src="../../../translated_images/zh-MO/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*有嚴重度等級的一致程式碼審查框架*

**多輪聊天** - 適用需保留上下文的對話。模型記住先前訊息並基於它們回答。用於互動協助或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-MO/context-memory.dff30ad9fa78832a.webp" alt="上下文記憶" width="800"/>

*對話上下文累積多輪直到達令牌限制*

**逐步推理** - 適用需明確邏輯的問題。模型展現每步的推理。用於數學、邏輯謎題，或需理解思考過程。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-MO/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="逐步推理模式" width="800"/>

*將問題拆解為明確邏輯步驟*

**受限輸出** - 對回答有特定格式要求時。模型嚴格遵守格式和長度規則。用於摘要，或需精確輸出結構。

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

<img src="../../../translated_images/zh-MO/constrained-output-pattern.0ce39a682a6795c2.webp" alt="受限輸出模式" width="800"/>

*強制特定格式、長度與結構要求*

## 使用現有 Azure 資源

**核實部署狀態：**

確保根目錄存在 `.env` 檔案，含 Azure 認證（模組 01 過程建立）：
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用：**

> **注意：** 若你已使用模組 01 的 `./start-all.sh` 啟動所有應用，本模組已在 8083 埠口運行。你可跳過下述啟動指令，直接前往 http://localhost:8083。

**方案 1：使用 Spring Boot 儀表板（推薦 VS Code 用戶）**

開發容器包含 Spring Boot 儀表板擴充，提供視覺介面管理所有 Spring Boot 應用。可於 VS Code 左側活動欄中找到（尋找 Spring Boot 圖標）。

你可以透過 Spring Boot 儀表板：
- 查看工作區內所有可用的 Spring Boot 應用
- 一鍵啟動/停止應用
- 實時查看應用日誌
- 監控應用狀態
只需按一下「prompt-engineering」旁邊的播放鈕，即可開始此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot 儀表板" width="400"/>

**選項二：使用 shell 腳本**

啟動所有網頁應用程式（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄開始
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 由根目錄開始
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

兩個腳本會自動從根目錄的 `.env` 檔案載入環境變數，並會在 JAR 檔案不存在時進行建置。

> **注意：** 如果你偏好在啟動之前手動建置所有模組：
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

在你的瀏覽器中開啟 http://localhost:8083 。

**停止方法：**

**Bash:**
```bash
./stop.sh  # 僅限此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅限此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-MO/dashboard-home.5444dbda4bc1f79d.webp" alt="儀表板首頁" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主儀表板顯示所有 8 種 prompt engineering 模式及其特點和使用案例*

## 探索各種模式

網頁介面讓你可以試驗不同的提示策略。每種模式解決不同的問題——試試看，看看什麼時候每個方法最有效。

> **注意：串流與非串流** — 每個模式頁面都有兩個按鈕：**🔴 串流回應（即時）** 和 **非串流** 選項。串流使用伺服器發送事件 (SSE) 來實時顯示模型生成的標記，因此你能立即看到進度。非串流則會等整個回應完成後才顯示。對於觸發深度推理的提示（例如，高熱忱、自我反思程式碼）非串流呼叫可能耗時很長——有時甚至幾分鐘——且沒有可見回饋。**在試驗複雜提示時建議使用串流**，這樣你能看到模型的運作，並避免以為請求已逾時。
>
> **注意：瀏覽器要求** — 串流功能使用 Fetch Streams API（`response.body.getReader()`），需要完整瀏覽器（Chrome、Edge、Firefox、Safari）。它在 VS Code 內建的簡單瀏覽器中**無法運作**，因為其 Webview 不支援 ReadableStream API。如果你使用簡單瀏覽器，非串流按鈕仍然能正常使用——只有串流按鈕會受影響。請在外部瀏覽器開啟 `http://localhost:8083` 以獲得完整體驗。

### 低熱忱與高熱忱

用低熱忱模式問一個簡單問題，例如「200 的 15% 是多少？」你會立即得到直接答案。現在用高熱忱模式問一個複雜問題，例如「設計一個高流量 API 的快取策略」。點擊 **🔴 串流回應（即時）**，觀看模型逐字元展示詳盡推理。同樣的模型、同樣的問題結構——但提示告訴它思考的深度。

### 任務執行（工具前置說明）

多步驟工作流程受益於事先規劃和進度敘述。模型先概述將做什麼，然後敘述每一步，最後總結結果。

### 自我反思程式碼

嘗試「建立一個電子郵件驗證服務」。模型不只單純產生程式碼並停止，而是生成後，根據品質標準評估，找出弱點並改進。你會看到它反覆迭代直到程式碼符合生產標準。

### 結構化分析

程式碼審查需要一致的評估框架。模型以固定類別（正確性、做法、效能、安全性）分析程式碼，並根據嚴重程度來評分。

### 多輪對話

問「什麼是 Spring Boot？」然後立即接著問「給我一個範例」。模型會記得你第一個問題，並特別給出 Spring Boot 範例。沒有記憶功能，第二個問題會太模糊。

### 逐步推理

選擇一道數學題，用逐步推理和低熱忱兩種方式嘗試。低熱忱只給你答案——快速但不透明。逐步推理則顯示每個計算和決策過程。

### 限制輸出

當你需要特定格式或字數時，這種模式強制嚴格遵守。嘗試產生一個精確 100 字的摘要，並以點列格式呈現。

## 你真正學到的是什麼

**推理投入改變一切**

GPT-5.2 讓你透過提示控制計算努力度。低投入意味著快速回應且探索有限。高投入意味著模型花時間深入思考。你正在學如何將努力度與任務複雜度相匹配——簡單問題別浪費時間，複雜決策也別太匆忙。

**結構引導行為**

注意提示中的 XML 標籤嗎？它們不是裝飾。模型更可靠地遵循結構化指令勝過自由文本。需要多步驟流程或複雜邏輯時，結構有助於模型追蹤位置與下一步。

<img src="../../../translated_images/zh-MO/prompt-structure.a77763d63f4e2f89.webp" alt="提示結構" width="800"/>

*一個結構良好的提示範例，擁有清楚的區塊和 XML 樣式組織*

**品質透過自我評估**

自我反思模式明確表達品質標準。不用期待模型「做對」，你告訴模型什麼是「正確」：邏輯、錯誤處理、效能、安全性。模型接著可以評估自身輸出並改進。這讓程式碼生成從一種抽獎變成可控過程。

**上下文是有限的**

多輪對話透過每次請求附帶訊息歷史運作。但有上限——每個模型都有最大標記數。隨著對話增長，你將需要策略來保留相關上下文而不觸及上限。這個模組展示記憶的運作；稍後你會學到何時摘要、何時忘記、何時重新提取。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導航：** [← 上一個：模組 01 - 介紹](../01-introduction/README.md) | [返回主頁](../README.md) | [下一個：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。文件的原始語言版本應視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們對因使用本翻譯而引致的任何誤解或誤譯概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->