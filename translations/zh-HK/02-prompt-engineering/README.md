# Module 02: 使用 GPT-5.2 進行提示工程

## 目錄

- [影片導覽](../../../02-prompt-engineering)
- [你將學習到的內容](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少量範例提示](../../../02-prompt-engineering)
  - [思維鏈](../../../02-prompt-engineering)
  - [角色式提示](../../../02-prompt-engineering)
  - [提示範本](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用既有的 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低熱情與高熱情的差異](../../../02-prompt-engineering)
  - [任務執行（工具前置詞）](../../../02-prompt-engineering)
  - [自我反思程式碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多回合聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 影片導覽

觀看這場直播，說明如何開始本模組：[Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## 你將學習到的內容

<img src="../../../translated_images/zh-HK/what-youll-learn.c68269ac048503b2.webp" alt="你將學習到的內容" width="800"/>

在上一個模組，你了解了記憶如何促進對話式 AI，並使用 GitHub Models 進行基本互動。現在我們將聚焦於如何提問——也就是提示本身，採用 Azure OpenAI 的 GPT-5.2。你如何結構提示，會深刻影響回應的品質。我們先回顧基本的提示技巧，然後介紹八個進階模式，充分利用 GPT-5.2 的能力。

我們選用 GPT-5.2，因為它引入了推理控制——你可以告訴模型在回答前要思考多少。這使各種提示策略更明顯，幫助你明白何時使用哪種方法。相比 GitHub Models，我們也能利用 Azure 對 GPT-5.2 較少的速率限制。

## 先決條件

- 已完成模組 01（部署 Azure OpenAI 資源）
- 根目錄中有 `.env` 檔案，包含 Azure 憑證（由模組 01 的 `azd up` 建立）

> **注意：** 如果還沒完成模組 01，請先依照當中的部署說明操作。

## 理解提示工程

<img src="../../../translated_images/zh-HK/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文字，使你穩定獲得所需結果的藝術。它不只是問問題，還要結構化請求，讓模型完全理解你想要什麼以及該如何提供。

想像你在給同事指示。「修復錯誤」太模糊，「在 UserService.java 第 45 行加入 null 檢查修復 Null Pointer Exception」就很具體。語言模型也是一樣——具體與結構化很重要。

<img src="../../../translated_images/zh-HK/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 如何整合" width="800"/>

LangChain4j 提供了基礎架構——模型連接、記憶、訊息類型；而提示模式則是你透過該架構傳送的精心結構化文字。主要組件是 `SystemMessage`（設定 AI 的行為與角色）與 `UserMessage`（傳遞你的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-HK/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式概覽" width="800"/>

在深入本模組的進階模式前，先回顧五種基礎提示技巧。這是每位提示工程師必備的構件。如果你已經完成了[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，你已經見過這些操作過程——這是幕後的概念架構。

### 零次提示

最簡單的方式：直接給模型指令，不提供範例。模型全憑訓練資料理解執行任務。這適用於簡單明確的請求。

<img src="../../../translated_images/zh-HK/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次提示" width="800"/>

*直接指令，無範例——模型僅由指令推測任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應：「正面」
```

**使用時機：** 簡單分類、直接提問、翻譯，或任何模型能獨立完成的任務。

### 少量範例提示

提供範例示範你希望模型遵循的模式。模型從你的範例學習輸入輸出格式並應用於新輸入，極大提升格式或行為不明顯任務的穩定性。

<img src="../../../translated_images/zh-HK/few-shot-prompting.9d9eace1da88989a.webp" alt="少量範例提示" width="800"/>

*從範例學習——模型辨識模式並套用於新輸入*

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

**使用時機：** 自訂分類、一致格式、特定領域任務，或零次提示結果不穩定情況。

### 思維鏈

要求模型逐步顯示推理過程。模型不直接給出答案，而是拆解問題、逐步推演。提升數學、邏輯和多步推理任務的準確度。

<img src="../../../translated_images/zh-HK/chain-of-thought.5cff6630e2657e2a.webp" alt="思維鏈提示" width="800"/>

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

**使用時機：** 數學題、邏輯謎題、除錯，或需要展示推理過程以提升準確性與信賴的任務。

### 角色式提示

在提問前設定 AI 擔任的角色或身份。提供上下文，塑造回應的語氣、深度和焦點。「軟體架構師」給的建議與「初級開發者」或「安全審查員」不同。

<img src="../../../translated_images/zh-HK/role-based-prompting.a806e1a73de6e3a4.webp" alt="角色式提示" width="800"/>

*設定上下文與角色——相同問題因角色不同而得到不同答案*

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

**使用時機：** 程式碼審查、教學、領域特定分析，或需針對專業層級或觀點調整回應時。

### 提示範本

建立可重複使用的提示範本，含變數佔位符。無需每次寫新提示，一次定義範本並輸入不同值。LangChain4j 的 `PromptTemplate` 類別以 `{{variable}}` 語法實現此功能。

<img src="../../../translated_images/zh-HK/prompt-templates.14bfc37d45f1a933.webp" alt="提示範本" width="800"/>

*含變數佔位符的可重用提示——一個範本，多重用途*

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

**使用時機：** 重複查詢不同輸入、批次處理、構建可重用 AI 工作流程，或提示結構相同但資料不同情境。

---

這五項基礎為你提供大部分提示任務所需工具包。本模組其餘部分以 **八個進階模式** 建構，充分發揮 GPT-5.2 的推理控制、自我評估和結構化輸出能力。

## 進階模式

基礎準備完後，讓我們進入本模組獨特的八個進階模式。並非所有問題都需相同方式。有些問題需快速答覆，有些需深入思考。有些需顯示推理，有些只要結果。以下每個模式針對不同場景最佳化——GPT-5.2 的推理控制更突顯差異。

<img src="../../../translated_images/zh-HK/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示模式" width="800"/>

*八種提示工程模式及其使用場景概覽*

<img src="../../../translated_images/zh-HK/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 推理控制" width="800"/>

*GPT-5.2 推理控制讓你指定模型思考多少——從快速直接到深度探究*

**低熱情（快速且專注）** — 適用於你需要快速、直接答案的簡單問題。模型推理步數最少，最多 2 步。用於計算、查詢或直接問題。

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

> 💡 **用 GitHub Copilot 探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)，詢問：
> - 「低熱情與高熱情提示模式有何差異？」
> - 「提示中的 XML 標籤如何幫助結構 AI 回應？」
> - 「何時該用自我反思模式，何時用直接指令？」

**高熱情（深度且徹底）** — 用於你想要全面分析的複雜問題。模型徹底探討並展示詳細推理。適用系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** — 適用多步工作流程。模型先給出計劃，過程中逐步說明並最終總結。用於遷移、實作或任何多步驟流程。

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

思維鏈提示明確要求模型顯示推理過程，提升複雜任務準確度。逐步拆解幫助人類和 AI 理解邏輯。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天嘗試：** 詢問模式相關：
> - 「如何調整任務執行模式以支援長時間運行操作？」
> - 「生產環境中結構工具前置詞的最佳實踐是什麼？」
> - 「如何擷取並顯示介面上的中間進度更新？」

<img src="../../../translated_images/zh-HK/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結的多步任務工作流程*

**自我反思程式碼** — 產出生產級程式碼。模型依生產標準生成，含適當錯誤處理。用於開發新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-HK/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改進循環——生成、評估、問題識別、改進、重複*

**結構化分析** — 用固定框架進行一致性評估。模型依正確性、實務、效能、安全、可維護性進行程式碼審查。適用程式碼審查或品質評估。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天嘗試：** 詢問結構化分析：
> - 「如何針對不同審查類型自訂分析框架？」
> - 「用程式方式解析並行動結構化輸出的最佳方法？」
> - 「如何在不同審查會議保持嚴重性評等一致？」

<img src="../../../translated_images/zh-HK/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*具有嚴重程度等級的一致程式碼審查框架*

**多回合聊天** — 用於需要上下文的對話。模型記住先前訊息並基於此繼續。適合互動式協助或複雜問答。

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

*多回合對話累積上下文，直到達到字元限制*

**逐步推理** — 適用需可見邏輯問題。模型展示每一步明確推理。用於數學、邏輯題，或需了解思考過程時。

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

**受限輸出** — 回應需特定格式限制。模型嚴格遵守格式和長度規定。用於摘要或需要精確輸出結構時。

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

*強制特定格式、長度與結構要求*

## 使用既有的 Azure 資源

**確認部署：**

確定根目錄有包含 Azure 憑證的 `.env` 檔案（模組 01 建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已在模組 01 使用 `./start-all.sh` 啟動所有應用，本模組已在 8083 埠執行。可跳過以下啟動指令，直接前往 http://localhost:8083。

**選項一：使用 Spring Boot Dashboard（推薦給 VS Code 使用者）**
開發容器包含了 Spring Boot Dashboard 擴充功能，提供可視化介面來管理所有 Spring Boot 應用程式。你可以在 VS Code 左側的活動列中找到它（尋找 Spring Boot 圖示）。

從 Spring Boot Dashboard，你可以：
- 查看工作區內所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 實時查看應用程式日誌
- 監控應用程式狀態

只要點擊 "prompt-engineering" 旁的播放按鈕即可啟動此模組，或一次啟動所有模組。

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

兩個腳本會自動從根目錄的 `.env` 檔案載入環境變數，若 JAR 檔不存在將會自動建立。

> **注意：** 如果你想先手動編譯所有模組再啟動：
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
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-HK/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主控台顯示所有 8 種提示工程模式及其特性與使用案例*

## 探索這些模式

網頁介面讓你試驗不同的提示策略。每個模式解決不同問題——試試看，看看哪種方法最合適。

> **注意：串流與非串流** — 每個模式頁面都有兩個按鈕：**🔴 串流回應（實時）** 和 **非串流** 選項。串流使用伺服器發送事件（SSE）實時顯示模型產生的 token，即時看到進度。非串流選項則等到完整回應後才顯示。對於需要深入推理的提示（例如 高熱忱模式、反思程式碼），非串流呼叫可能需要很長時間——有時幾分鐘——且沒有顯示進度。**試驗複雜提示時建議使用串流，這樣你能看到模型的運作，且避免誤以為請求超時。**
>
> **注意：瀏覽器需求** — 串流功能使用 Fetch Streams API（`response.body.getReader()`），僅支援完整瀏覽器（Chrome、Edge、Firefox、Safari）。VS Code 內建的簡易瀏覽器由於其 webview 不支援 ReadableStream API，無法正常使用該功能。使用簡易瀏覽器時，非串流按鈕仍可正常使用，僅串流按鈕失效。請在外部瀏覽器開啟 `http://localhost:8083` 以獲得完全體驗。

### 低熱忱 vs 高熱忱

用低熱忱模式問一個簡單問題：「200 的 15% 是多少？」你會立刻得到直接答案。再用高熱忱模式問個複雜問題：「為高流量 API 設計快取策略」，點擊 **🔴 串流回應（實時）**，你會一邊看到模型逐字推理。相同的模型、相同的問題結構，但提示告訴它需要多深的思考。

### 任務執行（工具前置）

多步驟工作流程適合事先規劃和過程敘述。模型先說明計畫，再一步步敘述，最後總結結果。

### 反思式程式碼

試試「建立一個電子郵件驗證服務」。模型不只是產生程式碼並停止，而是會先產生、依照品質標準評估、找出弱點，然後改進。你會看到程式碼反覆被改良直到達到生產標準。

### 結構化分析

程式碼審查需要一致的評估框架。模型依固定類別（正確性、實務、效能、安全性）和嚴重度分析程式碼。

### 多輪對話

問「什麼是 Spring Boot？」後立刻追問「給我一個範例」。模型會記住你的第一個問題並特定給出 Spring Boot 範例。沒記憶功能時，第二個問題會太模糊。

### 逐步推理

挑一個數學題，用逐步推理和低熱忱模式比較。低熱忱直接給解答——快但不透明。逐步推理則向你展示每個計算和決策。

### 限制輸出格式

需要特定格式或字數時，這種模式會嚴格執行。試試生成剛好 100 字的摘要清單。

## 你真正學到的是什麼

**推理用力改變一切**

GPT-5.2 讓你透過提示控制計算用力。低用力反應快、探索少。高用力讓模型花時間深度思考。你學會根據任務複雜度匹配用力——不要浪費時間在簡單問題，但複雜決策也別急躁。

**結構引導行為**

注意提示中的 XML 標籤？它們不是裝飾。模型比起自由文本更可靠地遵循結構化指令。當需要多步驟流程或複雜邏輯時，結構幫助模型追蹤位置與接下來的動作。

<img src="../../../translated_images/zh-HK/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*結構良好提示的組成，有清晰分段和 XML 樣式組織*

**品質來自自我評估**

反思式模式透過明確品質標準實現。你不需要只希望模型「做對」，你要告訴它「對」的標準：邏輯正確、錯誤處理、效能、安全。模型隨後可評估自己輸出並改進。這讓程式碼生成從抽獎變成過程。

**上下文是有限的**

多輪對話通過每次請求包含訊息歷史來運作。但有上限——每個模型有最大 token 數量。隨著對話增長，你需要策略保持相關上下文同時避免超限。這個模組示範記憶如何運作；以後你會學習何時總結、何時忘記、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG (檢索增強生成)](../03-rag/README.md)

---

**導覽：** [← 上一頁：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一頁：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件是通過 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於重要資訊，建議由專業人工翻譯。本公司不對因使用本翻譯而引起的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->