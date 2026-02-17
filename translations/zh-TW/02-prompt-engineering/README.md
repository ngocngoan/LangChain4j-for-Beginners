# 模組 02：使用 GPT-5.2 進行提示工程

## 目錄

- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少量示例提示](../../../02-prompt-engineering)
  - [思路鏈](../../../02-prompt-engineering)
  - [角色基礎提示](../../../02-prompt-engineering)
  - [提示範本](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低熱情 vs 高熱情](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思程式碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你將學到什麼

<img src="../../../translated_images/zh-TW/what-youll-learn.c68269ac048503b2.webp" alt="你將學到什麼" width="800"/>

在上一個模組中，你了解了記憶如何賦能會話式 AI，並使用 GitHub Models 進行基礎交互。現在我們將聚焦於如何提問——提示本身——使用 Azure OpenAI 的 GPT-5.2。你如何構造提示會極大影響回應品質。我們先回顧基本的提示方法，接著進入利用 GPT-5.2 功能的八種進階模式。

我們使用 GPT-5.2 是因為它引入了推理控制——你可以告訴模型在回答前要思考多少。這使得不同的提示策略更易辨識，有助於你了解何時使用哪種方式。與 GitHub Models 相比，我們也能享受 Azure 在 GPT-5.2 上較少的速率限制。

## 先決條件

- 完成模組 01（部署 Azure OpenAI 資源）
- 根目錄下有包含 Azure 憑證的 `.env` 檔案（由模組 01 中的 `azd up` 建立）

> **注意：** 如果尚未完成模組 01，請先依照那裡的部署指示操作。

## 理解提示工程

<img src="../../../translated_images/zh-TW/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文字，使你能持續獲得所需結果的藝術。不只是提問——而是結構化請求，讓模型明確理解你想要什麼以及如何回應。

可以把它想像為給同事下指令。「修復錯誤」很模糊。「修復 UserService.java 第 45 行的空指標例外，方法是加上空值檢查」就很具體。語言模型的工作原理也一樣——具體與結構化相當重要。

<img src="../../../translated_images/zh-TW/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的角色" width="800"/>

LangChain4j 提供基礎架構——模型連接、記憶與訊息類型——而提示模式就是你透過這些架構傳送的精心結構化文本。主要組件是 `SystemMessage`（設定 AI 行為與角色）和 `UserMessage`（攜帶你實際的請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-TW/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式總覽" width="800"/>

在深入本模組的進階模式前，讓我們回顧五種基礎的提示技術。這些是每位提示工程師應熟悉的基石。如果你已完成[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，你一定對它們不陌生——這裡是背後的概念架構。

### 零次提示

最簡單的方式：給模型直接指示，沒有範例。模型完全靠訓練來理解與執行任務。適用於明確且簡單的請求。

<img src="../../../translated_images/zh-TW/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次提示" width="800"/>

*不帶範例的直接指示——模型僅從指示判斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應："正面"
```

**使用時機：** 簡單分類、直接問題、翻譯或任何模型可獨立處理的任務。

### 少量示例提示

提供範例展示你想要模型遵照的模式。模型從範例中學習輸入輸出格式，套用到新輸入。對格式或行為不明顯的任務，能大幅提升一致性。

<img src="../../../translated_images/zh-TW/few-shot-prompting.9d9eace1da88989a.webp" alt="少量示例提示" width="800"/>

*從範例學習——模型辨識模式並應用於新輸入*

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

**使用時機：** 自訂分類、一致格式化、專業領域任務或零次提示結果不穩定時。

### 思路鏈

要求模型展示逐步推理過程。不直接給出答案，而是分解問題，逐步推導。提升數學、邏輯與多步推理任務的準確度。

<img src="../../../translated_images/zh-TW/chain-of-thought.5cff6630e2657e2a.webp" alt="思路鏈提示" width="800"/>

*逐步推理——將複雜問題拆解成清晰邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 圖中顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**使用時機：** 數學題、邏輯謎題、除錯或需要展示推理過程以增進信任與準確性的任務。

### 角色基礎提示

在提問前設定 AI 的身分或角色。此上下文影響回答的語氣、深度與重點。「軟體架構師」給出的建議就不同於「初級開發者」或「資安審核員」。

<img src="../../../translated_images/zh-TW/role-based-prompting.a806e1a73de6e3a4.webp" alt="角色基礎提示" width="800"/>

*設定上下文與身分——同一問題依指定角色獲得不同回答*

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

**使用時機：** 程式碼審查、教學、專業領域分析，或需要依專家層級量身回答時。

### 提示範本

建立可重複使用的提示並含有變量佔位符。不必每次重新撰寫提示，只要定義範本並填入不同數值即可。LangChain4j 的 `PromptTemplate` 類使用 `{{variable}}` 語法非常方便。

<img src="../../../translated_images/zh-TW/prompt-templates.14bfc37d45f1a933.webp" alt="提示範本" width="800"/>

*帶變數佔位符的可重複提示——一個範本，多種用途*

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

**使用時機：** 重複查詢不同輸入、批次處理、構建可重用 AI 工作流程，或提示架構固定但資料變動時。

---

這五項基礎技巧為大多數提示任務奠定紮實基礎。接下來本模組將基於它們，介紹**八種進階模式**，充分利用 GPT-5.2 的推理控制、自我評估與結構化輸出能力。

## 進階模式

基礎掌握後，讓我們進入本模組獨有的八種進階提示模式。不同問題不需用相同方式；有些需快速回答、有些需深度思考；有些需可見推理、有些只要結果。以下每種模式針對特定場景最佳化——而 GPT-5.2 的推理控制讓這些差異更明顯。

<img src="../../../translated_images/zh-TW/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示模式" width="800"/>

*八種提示工程模式及其適用場景總覽*

<img src="../../../translated_images/zh-TW/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 的推理控制允許你指定模型思考深度——從快速直接答覆到深層探索*

<img src="../../../translated_images/zh-TW/reasoning-effort.db4a3ba5b8e392c1.webp" alt="推理努力程度比較" width="800"/>

*低熱情（快速直接）與高熱情（細緻探索）推理策略比較*

**低熱情（快速且聚焦）** - 適用簡單問題且想要快速直接答案。模型只做最少推理——最多兩步。適用於計算、查詢或直接問題。

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
> - 「低熱情與高熱情提示模式有何差異？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「何時該使用自我反思模式與直接指示？」

**高熱情（深度且細緻）** - 適用複雜問題且需全面分析。模型會徹底探究並展示詳盡推理。適用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步推進）** - 適用多步驟工作流程。模型先提供計劃，執行過程中逐步講述，最後給出總結。用於遷移、實作或其他多步驟流程。

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

思路鏈提示明確要求模型展示推理過程，提升複雜任務準確度。分步解析有助人類與 AI 澄清邏輯。

> **🤖 嘗試 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 詢問此模式：
> - 「如何調整任務執行模式以適應長時間運行作業？」
> - 「生產環境中結構化工具前言的最佳實踐是什麼？」
> - 「如何在 UI 中捕捉並顯示中間進度更新？」

<img src="../../../translated_images/zh-TW/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結的多步工作流程*

**自我反思程式碼** - 適用於產出生產等級的程式碼。模型會依標準產出並包含妥善錯誤處理。適合開發新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-TW/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改進循環 - 生成、評估、找出問題、改進、重複*

**結構化分析** - 適用於一致性評估。模型使用固定框架（正確性、實務、效能、安全、可維護性）審查程式碼。用於程式碼審查或品質評估。

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

> **🤖 嘗試 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 詢問結構化分析：
> - 「如何為不同類型程式碼審查客製化分析框架？」
> - 「最有效的方式解析並程式化處理結構化輸出是什麼？」
> - 「如何確保不同審查回合嚴重度的一致性？」

<img src="../../../translated_images/zh-TW/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*具嚴重度等級的一致程式碼審查框架*

**多輪聊天** - 適用於需要上下文記憶的對話。模型會記住前次訊息並基於此回覆。用於互動支援或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-TW/context-memory.dff30ad9fa78832a.webp" alt="上下文記憶" width="800"/>

*多輪對話中上下文如何累積直到達標記限制*

**逐步推理** - 適用需顯示邏輯的問題。模型展示每步推理，幫助理解思考過程。用於數學題、邏輯謎題，或你想看見推理線索時。

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

*將問題拆解成清晰邏輯步驟*

**受限輸出** - 適用回應需嚴格符合格式要求。模型嚴格遵守格式與長度限制。用於摘要或需精確輸出結構的場景。

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

*執行具體格式、長度與結構限制*

## 使用現有 Azure 資源

**驗證部署：**

確認根目錄有包含 Azure 憑證的 `.env` 檔案（由模組 01 建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已使用模組 01 的 `./start-all.sh` 啟動所有應用，本模組已在 8083 埠運行。你可略過以下啟動命令，直接前往 http://localhost:8083。

**方式一：使用 Spring Boot Dashboard（建議 VS Code 使用者）**

開發容器包含 Spring Boot Dashboard 擴充元件，提供視覺化界面管理所有 Spring Boot 應用程式。你可以在 VS Code 左側活動欄中找到它（尋找 Spring Boot 圖示）。
從 Spring Boot 儀表板，您可以：
- 查看工作區中的所有可用 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 即時查看應用程式日誌
- 監控應用程式狀態

只需點擊「prompt-engineering」旁邊的播放按鈕即可啟動此模組，或同時啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項二：使用 shell 腳本**

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

兩個腳本會自動從根目錄的 `.env` 檔案載入環境變數，若 JAR 檔案不存在，會自動編譯。

> **注意：** 如果您想先手動編譯所有模組再啟動：
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

**停止應用程式：**

**Bash:**
```bash
./stop.sh  # 只有這個模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅限此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-TW/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主要儀表板顯示 8 種提示工程模式及其特性與使用場景*

## 探索模式

網頁介面讓您可以試驗不同的提示策略。每種模式解決不同問題——試試看，了解每種方法的優勢。

### 低熱忱 vs 高熱忱

使用低熱忱問一個簡單問題：「200 的 15% 是多少？」您會立即得到直接答覆。再用高熱忱問複雜問題：「為高流量 API 設計快取策略」，您會看到模型慢下來並給出詳細推理。是相同模型，相同問句結構，但提示決定了思考深度。

<img src="../../../translated_images/zh-TW/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*快速計算，推理簡潔*

<img src="../../../translated_images/zh-TW/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*完整快取策略 (2.8MB)*

### 任務執行（工具前言）

多步驟流程需要事先規劃與進度解說。模型會先說明要做什麼，逐步敘述每個步驟，最後總結結果。

<img src="../../../translated_images/zh-TW/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*逐步說明建立 REST 端點 (3.9MB)*

### 自我反思程式碼

試試「建立電子郵件驗證服務」。模型不只是生成程式碼然後停止，而是生成後評估質量標準，找出弱點並改進。您會看到模型反覆迭代直到程式碼達到量產標準。

<img src="../../../translated_images/zh-TW/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*完整電子郵件驗證服務 (5.2MB)*

### 結構化分析

程式碼審查需要一致的評估框架。模型使用固定類別（正確性、最佳實踐、效能、安全性）並標示嚴重程度來分析程式碼。

<img src="../../../translated_images/zh-TW/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*基於框架的程式碼審查*

### 多輪對話

問「Spring Boot 是什麼？」然後立刻追問「給我一個範例」。模型會記得第一個問題，給您針對 Spring Boot 的範例。沒有記憶，第二個問題會太過模糊。

<img src="../../../translated_images/zh-TW/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*跨問題保持上下文*

### 逐步推理

選一個數學問題，用逐步推理與低熱忱嘗試。低熱忱只給答案——快速但沒說明。逐步推理展示每個計算與決策過程。

<img src="../../../translated_images/zh-TW/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*帶明確步驟的數學問題*

### 約束輸出

需要特定格式或字數時，此模式嚴格遵守規定。試著生成一個 точно 100 字、使用點列格式的摘要。

<img src="../../../translated_images/zh-TW/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*具有格式控制的機器學習摘要*

## 您真正學到的

**推理努力改變一切**

GPT-5.2 讓您透過提示控制計算花費。低努力意味快速回覆、最小探索。高努力意味模型需要時間深入思考。您正在學會如何匹配任務複雜度的努力——簡單問題不浪費時間，複雜決策不輕率。

**結構引導行為**

注意提示中的 XML 標籤？不是裝飾。模型比起自由文本，更能可靠遵循結構化指令。當您需要多步驟過程或複雜邏輯時，結構能幫助模型追蹤所在步驟與下一步該做什麼。

<img src="../../../translated_images/zh-TW/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*結構明確的提示架構，清晰區段及 XML 風格組織*

**透過自我評估提升品質**

自我反思模式將品質標準明確列出。不再期待模型「做對」，而是告訴它「正確」代表什麼：邏輯正確、錯誤處理、效能、安全性。模型便可自我評估並改良。程式碼生成從賭博變成有章法的流程。

**上下文有限**

多輪對話靠每次請求包含訊息歷史。但有上限——每個模型都有最大 token 數。隨著對話成長，您需要策略來保持相關上下文而不超限。這個模組示範記憶如何運作；接下來您會學到何時摘要、何時遺忘、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG (檢索強化生成)](../03-rag/README.md)

---

**導覽：** [← 上一頁：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一頁：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們力求準確，請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯所引起的任何誤解或誤譯負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->