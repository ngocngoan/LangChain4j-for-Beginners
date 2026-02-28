# Module 02: 使用 GPT-5.2 進行提示工程

## 目錄

- [影片導覽](../../../02-prompt-engineering)
- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少量範例提示](../../../02-prompt-engineering)
  - [思維鏈](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示範本](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低 vs 高積極性](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [接下來步驟](../../../02-prompt-engineering)

## 影片導覽

觀看此現場會議，說明如何開始使用本模組：

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="使用 LangChain4j 進行提示工程 - 現場會議" width="800"/></a>

## 你將學到什麼

<img src="../../../translated_images/zh-HK/what-youll-learn.c68269ac048503b2.webp" alt="你將學到什麼" width="800"/>

在上一個模組中，你了解記憶如何支持對話式 AI，並使用 GitHub Models 進行基本互動。現在我們將聚焦於如何提出問題——即提示本身——使用 Azure OpenAI 的 GPT-5.2。你組織提示的方式會大大影響回應品質。我們先回顧基本提示技巧，然後進入八種考量 GPT-5.2 能力的進階模式。

我們使用 GPT-5.2，是因為它引入了推理控制功能——你可以告訴模型答覆前要思考多少。這讓不同提示策略更明顯，也幫助你了解何時使用每種方法。相較於 GitHub Models，Azure 對 GPT-5.2 的速率限制更少，我們也將受益於此。

## 先決條件

- 已完成模組 01（已部署 Azure OpenAI 資源）
- 根目錄下有 `.env` 文件，包含 Azure 認證（由模組 01 中的 `azd up` 建立）

> **注意：** 如果你尚未完成模組 01，請先依照那裡的部署指示進行。

## 理解提示工程

<img src="../../../translated_images/zh-HK/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文字，以穩定取得所需結果。它不僅是問問題——而是組織請求，讓模型完全理解你想要什麼，以及如何交付它。

想像你正在給同事指示。「修正錯誤」很模糊。「在 UserService.java 第 45 行加入判斷以修正 Null Pointer Exception」則具體明確。語言模型也是如此——具體和結構化很重要。

<img src="../../../translated_images/zh-HK/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的角色" width="800"/>

LangChain4j 提供基礎架構——模型連接、記憶與訊息類型——而提示模式本質上是你通過這個架構發送的精心結構化文字。關鍵組件是 `SystemMessage`（設定 AI 行為與角色）與 `UserMessage`（攜帶你的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-HK/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式總覽" width="800"/>

在深入本模組的進階模式前，先回顧五種基礎提示技巧。這是每位提示工程師該理解的构件。若你之前已經完成了[快速開始模組](../00-quick-start/README.md#2-prompt-patterns)，你就見過這些實例——這裡是背後的概念框架。

### 零次提示

最簡單方法：給模型直接指令而不附範例。模型完全依訓練資料理解並執行任務。對於明確簡單請求非常有效。

<img src="../../../translated_images/zh-HK/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次提示" width="800"/>

*直接指令無範例——模型僅從指令推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應:「積極」
```

**適用時機：** 簡單分類、直接提問、翻譯，或任何模型可無指導自行處理的任務。

### 少量範例提示

提供範例示範你希望模型遵循的模式。模型從範例學習輸入輸出格式，並套用於新輸入。對於格式或行為不明確的任務，極大提升一致性。

<img src="../../../translated_images/zh-HK/few-shot-prompting.9d9eace1da88989a.webp" alt="少量範例提示" width="800"/>

*從範例學習——模型識別模式並套用於新輸入*

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

**適用時機：** 自訂分類、一致格式、領域專用任務，或零次提示結果不穩定時。

### 思維鏈

請模型展示逐步推理。模型不直接給答案，而是分解問題，逐一處理。提升數學、邏輯及多步推理任務的準確度。

<img src="../../../translated_images/zh-HK/chain-of-thought.5cff6630e2657e2a.webp" alt="思維鏈提示" width="800"/>

*逐步推理——把複雜問題拆解為明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**適用時機：** 數學問題、邏輯謎題、除錯，或任何顯示推理能提升準確性與可信度的任務。

### 基於角色的提示

在問問題前設定 AI 人設或角色。這提供上下文，塑造語氣、深度與重點。「軟體架構師」的建議和「初階開發者」或「安全稽核員」不同。

<img src="../../../translated_images/zh-HK/role-based-prompting.a806e1a73de6e3a4.webp" alt="基於角色的提示" width="800"/>

*設定上下文與人設——同一問題因角色不同得到不同回答*

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

**適用時機：** 代碼審查、輔導、領域專業分析，或需要依專業程度或角度量身定制回應時。

### 提示範本

創造可重用的提示，包含變數佔位符。不必每次都寫新提示，只需定義一次範本，填入不同數值。LangChain4j 的 `PromptTemplate` 類使用 `{{variable}}` 語法實現這一點。

<img src="../../../translated_images/zh-HK/prompt-templates.14bfc37d45f1a933.webp" alt="提示範本" width="800"/>

*含變數佔位的可重用提示——一個範本，多種用途*

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

**適用時機：** 不同輸入重複查詢、批量處理、構建可重用 AI 工作流程，或任何提示結構固定但數據變動的場景。

---

這五個基礎提供大多數提示任務的穩固工具箱。本模組後續建立於此基礎上，介紹利用 GPT-5.2 推理控制、自我評估與結構化輸出能力的**八種進階模式**。

## 進階模式

基礎說明後，進入本模組獨有的八種進階模式。不同問題不必用同一方法。有的問題需要快速回答，有的需要深度思考。有的需要明顯的推理過程，有的著重結果。以下每個模式專為不同場景優化——GPT-5.2 的推理控制讓差異更明顯。

<img src="../../../translated_images/zh-HK/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示工程模式" width="800"/>

*八種提示工程模式及其使用場景總覽*

<img src="../../../translated_images/zh-HK/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 推理控制可指定模型思考程度——從快速直接答覆到深入探索*

**低積極性（快速且聚焦）** - 適用簡單問題，想快速直接回答。模型做最少推理，最多兩步。用於計算、查詢或簡單問題。

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

> 💡 **使用 GitHub Copilot 探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)，詢問：
> - 「低積極性和高積極性提示模式有何不同？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回答？」
> - 「什麼時候該用自我反思模式，什麼時候用直接指令？」

**高積極性（深入且全面）** - 適用複雜問題，需全面分析。模型徹底探索並展示詳盡推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** - 用於多步驟工作流程。模型先提出計劃，邊做邊敘述各步，最後給總結。用於遷移、實作或任何多步程序。

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

思維鏈提示明確要求模型展示推理過程，提升複雜任務的準確性。逐步拆解幫助人與 AI 理解邏輯。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：** 詢問此模式：
> - 「如何將任務執行模式應用於長時間運行的操作？」
> - 「生產環境中，工具前言結構的最佳實踐是什麼？」
> - 「如何捕捉並在 UI 中呈現中間進度更新？」

<img src="../../../translated_images/zh-HK/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結流程，用於多步任務*

**自我反思代碼** - 用於產出生產品質代碼。模型依生產標準生成代碼且有妥當錯誤處理。適用於新功能或服務開發。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-HK/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改進循環——產生、評估、找問題、改進、重複*

**結構化分析** - 用於一致性評估。模型按固定架構審核代碼（正確性、實務、效能、安全、維護性）。適合代碼審查或品質評測。

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

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：** 詢問結構化分析相關：
> - 「如何為不同類型代碼審查自訂分析框架？」
> - 「以程式方式解析並利用結構化輸出最佳方式是什麼？」
> - 「如何確保不同審查回合中嚴重程度分級一致？」

<img src="../../../translated_images/zh-HK/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*用於一致的代碼審查框架與嚴重度分級*

**多輪對話** - 用於需要上下文的對話。模型記住前訊息並基於此進展。適合互動式支援或複雜問答。

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

*對話上下文累積多輪，直到達到 token 限制*

**逐步推理** - 用於需要展示邏輯步驟的問題。模型展現每步明確推理。適合數學題、邏輯謎題，或你想理解思考過程的任務。

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

*將問題拆成明確邏輯步驟*

**受限輸出** - 用於需特定格式要求的回答。模型嚴格遵守格式與長度規則。適合摘要或需要精確輸出結構的場景。

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

*強制執行特定格式、長度與結構要求*

## 使用現有 Azure 資源

**確認部署：**

確保根目錄有 `.env` 文件，包含 Azure 認證（模組 01 部署期間建立）：
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已用模組 01 中的 `./start-all.sh` 啟動所有應用，本模組已在端口 8083 運行。可以跳過下面的啟動指令，直接前往 http://localhost:8083 。
**選項 1：使用 Spring Boot Dashboard（建議 VS Code 用戶）**

開發容器包括 Spring Boot Dashboard 擴展，提供可視化介面以管理所有 Spring Boot 應用程式。你可以在 VS Code 左側的活動欄中找到它（尋找 Spring Boot 圖示）。

在 Spring Boot Dashboard 中，你可以：
- 查看工作區中所有可用的 Spring Boot 應用程式
- 只需點擊一下即可啟動/停止應用程式
- 實時查看應用程式日誌
- 監控應用程式狀態

只需點擊「prompt-engineering」旁邊的播放按鈕即可啟動此模組，或者同時啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有 web 應用程式（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄
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

兩個腳本都會自動從根目錄的 `.env` 文件載入環境變數，並且如果 JAR 檔案不存在會自動編譯。

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

在瀏覽器中開啟 http://localhost:8083 。

**停止方法：**

**Bash:**
```bash
./stop.sh  # 只有這個模組
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

<img src="../../../translated_images/zh-HK/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主控制面板展示所有 8 個 prompt engineering 模式及其特性和使用案例*

## 探索各種模式

Web 介面讓你可以嘗試不同的 prompt 策略。每個模式解決不同的問題——試試看，看看每種方法何時最適用。

> **注意：串流與非串流** — 每個模式頁面均有兩個按鈕：**🔴 串流回應（即時）** 和 **非串流** 選項。串流使用 Server-Sent Events (SSE) 於模型生成過程中即時顯示 token，讓你即時看到進度。非串流選項則會等整個回應完成後再顯示。對於需要深度推理的 prompt（例如，高「渴望」模式、自我反思代碼），非串流呼叫可能非常耗時——有時甚至需要數分鐘——而且無任何可見反饋。**在實驗複雜 prompt 時，建議使用串流功能**，讓你能看到模型的工作過程，避免以為請求超時。
>
> **注意：瀏覽器需求** — 串流功能使用 Fetch Streams API (`response.body.getReader()`)，需要完整瀏覽器支持（Chrome、Edge、Firefox、Safari）。VS Code 內建的 Simple Browser 不支援此功能，因其 webview 不支持 ReadableStream API。如果使用 Simple Browser，非串流按鈕仍能正常工作，只有串流按鈕受影響。請於外部瀏覽器開啟 `http://localhost:8083` 以獲得完整功能。

### 低渴望 vs 高渴望

使用低渴望，問一個簡單問題，例如「200 的 15% 是多少？」你會即時獲得直接答案。現在用高渴望問一個複雜的問題，例如「設計一個高流量 API 的快取策略」。點擊 **🔴 串流回應（即時）**，觀看模型逐個 token 展示詳細推理。同一模型、相同問題結構，唯有 prompt 指示思考深度不同。

### 任務執行（工具前言）

多步驟工作流程須先行規劃並提供過程旁白。模型先概述計劃，然後說明每一步，最後總結結果。

### 自我反思代碼

試試「建立電子郵件驗證服務」。模型不再只是生成功能代碼並停下，它會生成代碼，根據質量標準自我評估，找出弱點並改進。你會看到它反覆迭代，直到程式碼達到生產標準。

### 結構化分析

代碼審查需要一致性評估框架。模型會根據固定分類（正確性、實踐、效能、安全性）及嚴重程度分析代碼。

### 多回合聊天

問「什麼是 Spring Boot？」然後緊接著問「給我一個範例」。模型會記住你的第一個問題，並針對 Spring Boot 提供專屬範例。若沒有記憶，第二個問題過於模糊無法回答。

### 逐步推理

選一道數學題，分別用逐步推理和低渴望嘗試。低渴望僅給結果——快速但不透明。逐步推理則展示所有計算與決策過程。

### 受限輸出

當你需要特定格式或字數時，此模式嚴格要求遵守。嘗試生成一段正好 100 字的摘要並以項目符號格式呈現。

## 你真正學到的是什麼

**推理投入改變一切**

GPT-5.2 讓你透過 prompt 控制計算投入程度。低投入代表快速度回應，探索有限。高投入表示模型花時間深思。你在學習如何根據任務複雜度調整投入——別浪費時間在簡單問題上，但也別急於做出複雜決策。

**結構引導行為**

注意 prompt 裡的 XML 標籤嗎？它們不是裝飾。模型能更可靠地遵循結構化指令，勝過自由文本。當你需要多步驟流程或複雜邏輯時，結構化幫助模型追蹤現階段位置和接下來要做什麼。

<img src="../../../translated_images/zh-HK/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*結構良好的 prompt 解剖，有清晰段落與 XML 風格的組織*

**透過自我評估提升品質**

自我反思模式透過明確質量標準運作。不再抱希望模型「做對」，而是明確告訴它「正確」定義為何：正確邏輯、錯誤處理、效能、安全性。模型可基於此評估自身輸出並改進。代碼生成不再像抽獎，而是系統化流程。

**上下文有限**

多回合對話依賴每次請求附帶訊息歷史。但有上限——每款模型都有最大 token 數。當對話變長，你需要策略維持相關上下文，避免超出限制。本模組演示記憶運作；後續你將學習何時總結、何時忘記、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導覽：** [← 上一章：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一章：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。應以原始語言的文件作為權威來源。對於重要資訊，建議採用專業人士的人工翻譯。我們不對因使用此翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->