# 模組 02：使用 GPT-5.2 的提示工程

## 目錄

- [影片導覽](../../../02-prompt-engineering)
- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零樣本提示](../../../02-prompt-engineering)
  - [少量示例提示](../../../02-prompt-engineering)
  - [思路鏈](../../../02-prompt-engineering)
  - [角色基礎提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [執行應用程式](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低急迫感與高急迫感](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思程式碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 影片導覽

觀看這段現場教學，說明如何開始使用本模組：

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="使用 LangChain4j 的提示工程 - 現場教學" width="800"/></a>

## 你將學到什麼

以下圖示概述了本模組中你將發展的關鍵主題和技能——從提示精煉技巧到你將遵循的逐步工作流程。

<img src="../../../translated_images/zh-MO/what-youll-learn.c68269ac048503b2.webp" alt="你將學到什麼" width="800"/>

在前一個模組中，你探索了與 GitHub 模型的基本 LangChain4j 互動，並瞭解到記憶如何使 Azure OpenAI 實現對話式 AI。現在我們將專注於你如何提問——提示本身——使用 Azure OpenAI 的 GPT-5.2。你構建提示的方式會大幅影響回答的品質。我們先回顧基本的提示技巧，接著進入利用 GPT-5.2 能力的八個進階模式。

我們選用 GPT-5.2，是因為它引入了推理控制——你可以告訴模型在回應前要思考多少。這讓不同的提示策略變得更明顯，並幫助你瞭解何時使用哪種方法。我們也將受益於 Azure 相較 GitHub 模型對 GPT-5.2 的較低限制率。

## 先決條件

- 已完成模組 01（Azure OpenAI 資源已部署）
- 根目錄下有 `.env` 檔案，內含 Azure 憑證（由模組 01 中的 `azd up` 建立）

> **注意：** 如果尚未完成模組 01，請先依照那裡的部署指南操作。

## 理解提示工程

本質上，提示工程是模糊指令和精確指令之間的差異，如下方比較所示。

<img src="../../../translated_images/zh-MO/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程旨在設計能穩定取得所需結果的輸入文字。它不只是在提問——而是組織請求，讓模型完全理解你想要什麼，以及該如何給出。

想像你在向同事下指示。「修正錯誤」很模糊。「在 UserService.java 第 45 行的 null pointer 例外中加入 null 檢查」則很具體。語言模型也是如此——具體性與結構很重要。

以下圖示說明 LangChain4j 如何融入這個過程——通過 SystemMessage 和 UserMessage 這些基本組件，將你的提示模式連接到模型。

<img src="../../../translated_images/zh-MO/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的角色" width="800"/>

LangChain4j 提供基礎設施——模型連接、記憶與訊息類型——而提示模式則是你傳送過此基礎設施的精心結構化文字。關鍵組成是 `SystemMessage`（設定 AI 行為與角色）以及 `UserMessage`（承載你實際的請求）。

## 提示工程基礎

以下五個核心技巧構成有效提示工程的基礎。每項技巧針對你與語言模型溝通的不同面向。

<img src="../../../translated_images/zh-MO/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式概覽" width="800"/>

在深入本模組的進階模式之前，讓我們回顧五種基礎提示技巧。這些是每位提示工程師應該掌握的基石。假如你已經完成了[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，你會見識到這些技巧的實際應用——以下是其概念框架。

### 零樣本提示

最簡單的方法：給模型直接指令而不附示例。模型完全依賴其訓練來理解並執行任務。這適用於期望行為明顯的簡單任務。

<img src="../../../translated_images/zh-MO/zero-shot-prompting.7abc24228be84e6c.webp" alt="零樣本提示" width="800"/>

*不帶示例的直接指令——模型僅從指令推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應：「正面」
```

**使用時機：** 簡單分類、直接問題、翻譯或任何模型無需額外指引便能處理的任務。

### 少量示例提示

提供範例來展示你希望模型遵循的模式。模型從你的範例學習預期的輸入輸出格式，並應用至新輸入。這大幅提升需要特定格式或行為的任務一致性。

<img src="../../../translated_images/zh-MO/few-shot-prompting.9d9eace1da88989a.webp" alt="少量示例提示" width="800"/>

*從範例學習——模型識別模式並應用於新輸入*

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

**使用時機：** 自訂分類、一致的格式設定、領域專屬任務，或零樣本結果不穩定時。

### 思路鏈

要求模型逐步展示其推理過程。模型不直接給出答案，而是分解問題，明確執行每個步驟。這提升數學、邏輯與多步推理任務的準確度。

<img src="../../../translated_images/zh-MO/chain-of-thought.5cff6630e2657e2a.webp" alt="思路鏈提示" width="800"/>

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

**使用時機：** 數學題、邏輯謎題、除錯，或任何展示推理過程可增加準確性與信任度的任務。

### 角色基礎提示

在提問前設定 AI 的身份或角色。此設定提供上下文，影響回答的語氣、深度與焦點。像是「軟體架構師」給出建議，會與「初級開發者」或「安全稽核員」不同。

<img src="../../../translated_images/zh-MO/role-based-prompting.a806e1a73de6e3a4.webp" alt="角色基礎提示" width="800"/>

*設定上下文與角色——相同問題因分配角色而有不同回答*

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

**使用時機：** 代碼審查、輔導、領域專屬分析，或需要依據不同專業水準或視角給出回應時。

### 提示模板

創建可重複使用且帶有變數佔位符的提示。不是每次都寫新提示，而是定義一份模板，然後填入不同的變數。LangChain4j 的 `PromptTemplate` 類用 `{{variable}}` 語法輕鬆實現。

<img src="../../../translated_images/zh-MO/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*帶變數佔位符的可重用提示——一個模板，多種用途*

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

**使用時機：** 重複查詢帶不同輸入、批量處理、建立可重複使用的 AI 工作流程，或任何提示結構相同但數據不同的場景。

---

這五大基礎提供了絕大多數提示任務的堅實工具包。本模組餘下內容建立於此，聚焦 GPT-5.2 的推理控制、自我評估與結構化輸出功能的 **八個進階模式**。

## 進階模式

基礎技能已覆蓋，接下來進入讓本模組獨特的八種進階模式。不是所有問題都需要一致方法。有些需要快速回答，有些則須深入思考。有的需要顯示推理過程，有的只要結果。以下模式各適不同場景——而 GPT-5.2 的推理控制使差異更為顯著。

<img src="../../../translated_images/zh-MO/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示工程模式" width="800"/>

*八種提示工程模式及其使用案例概覽*

GPT-5.2 為這些模式加上一維度：*推理控制*。下方滑桿展示如何調整模型的思考力度——從快速直接回應到深入詳盡分析。

<img src="../../../translated_images/zh-MO/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 的推理控制讓你指定模型應思考多少——從快速直接回應到深入探索皆可*

**低急迫感（快速且聚焦）** - 適用於需要快速、直接回答的簡單問題。模型進行最少推理，最多兩步。用於計算、查詢或直接問題。

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
> - 「低急迫感和高急迫感提示模式有何差異？」
> - 「提示中的 XML 標籤如何幫助架構 AI 回答？」
> - 「何時該使用自我反思模式，何時該使用直接指令？」

**高急迫感（深度且全面）** - 適用於需要全面分析的複雜問題。模型徹底探索並展示詳盡推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** - 適用多步工作流程。模型提供事前規劃，邊做邊敘述每步，最後給出總結。適用於遷移、實作或任何多步流程。

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

思路鏈提示明確要求模型展示推理過程，提升複雜任務的準確度。逐步拆解有助於人與 AI 理解邏輯。

> **🤖 試試 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 詢問此模式：
> - 「如何調整任務執行模式以處理長時間運行操作？」
> - 「生產環境中如何最佳結構化工具前言？」
> - 「如何在 UI 中捕捉並顯示中間進度更新？」

以下圖說明此規劃 → 執行 → 總結工作流程。

<img src="../../../translated_images/zh-MO/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*多步任務的規劃 → 執行 → 總結流程*

**自我反思程式碼** - 用於產出生產等級代碼。模型生成符合生產標準且具適當錯誤處理的程式碼。用於建置新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

下圖示範此反覆迭代改進迴圈——生成、評估、找出弱點，並持續優化直至符合生產標準。

<img src="../../../translated_images/zh-MO/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改進迴圈——生成、評估、識別問題、改善、重複*

**結構化分析** - 用於一致性評估。模型使用固定架構檢視代碼（正確性、實務、效能、安全、可維護性）。用於程式碼審查或品質評估。

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

> **🤖 試試 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 詢問結構化分析：
> - 「如何針對不同類型程式碼審查自訂分析框架？」
> - 「程式化解析並處理結構化輸出的最佳方式是什麼？」
> - 「如何確保不同審查會議中嚴重性等級一致？」

下圖顯示此結構化框架如何組織程式碼審查，將檢視內容劃分為一致類別並配有嚴重性等級。

<img src="../../../translated_images/zh-MO/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*帶嚴重性等級的一致性程式碼審查框架*

**多輪對話** - 適合需要上下文的對話。模型記憶前訊息並基於此持續構建回應。適用於互動式協助或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

下圖視覺化說明對話上下文如何隨輪次累積以及它與模型 token 限制的關係。

<img src="../../../translated_images/zh-MO/context-memory.dff30ad9fa78832a.webp" alt="上下文記憶" width="800"/>

*對話上下文隨多輪累積直到達到 token 限制*
**逐步推理** - 用於需要顯示邏輯的問題。模型會對每一步驟進行明確推理。適用於數學問題、邏輯謎題或當你需要理解思考過程時使用。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

下圖展示模型如何將問題拆分成明確編號的邏輯步驟。

<img src="../../../translated_images/zh-MO/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*將問題拆解為明確的邏輯步驟*

**受限輸出** - 用於具有特定格式要求的回應。模型嚴格遵守格式和長度規則。適用於摘要或需要精確輸出結構時。

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

下圖展示約束如何引導模型產生嚴格遵守格式和長度要求的輸出。

<img src="../../../translated_images/zh-MO/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*強制執行特定格式、長度和結構要求*

## 運行應用程式

**驗證部署：**

確保在根目錄存在帶有 Azure 憑證的 `.env` 檔案（在模組 01 創建）。從模組目錄 (`02-prompt-engineering/`) 執行此命令：

**Bash:**
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已經從根目錄使用 `./start-all.sh` 啟動所有應用程式（如模組 01 所述），則此模組已在 8083 埠運行。你可以跳過以下啟動命令，直接訪問 http://localhost:8083。

**選項 1：使用 Spring Boot 儀表板（推薦 VS Code 用戶）**

開發容器包含 Spring Boot 儀表板擴充套件，提供視覺化介面管理所有 Spring Boot 應用程式。你可以在 VS Code 左側的活動列找到它（尋找 Spring Boot 圖示）。

透過 Spring Boot 儀表板，你可以：
- 查看工作區中所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 實時查看應用程式日誌
- 監控應用程式狀態

只需點擊 "prompt-engineering" 旁的播放按鈕啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot 儀表板 — 從一處啟動、停止並監控所有模組*

**選項 2：使用 Shell 腳本**

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

或單獨啟動此模組：

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

兩個腳本會自動從根目錄的 `.env` 檔案載入環境變數，並在不存在 JAR 時進行編譯。

> **注意：** 若你想在啟動前手動編譯所有模組：
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

在你的瀏覽器中打開 http://localhost:8083 。

**停止：**

**Bash:**
```bash
./stop.sh  # 僅限於此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只有此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

這是 prompt engineering 模組的主介面，你可以並排嘗試所有八種範式。

<img src="../../../translated_images/zh-MO/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主儀表板展示所有 8 種提示工程範式及其特點和使用案例*

## 探索範式

網頁介面讓你嘗試不同的提問策略。每個範式解決不同問題 — 嘗試看看何時每種方法最合適。

> **注意：串流與非串流** — 每個範式頁面均提供兩個按鈕：**🔴 串流回應（即時）** 與 **非串流** 選項。串流使用伺服器發送事件（SSE）即時顯示模型生成的字元，因此你能立即看到回應進度。非串流選項則等待整個回應完成才顯示。對於觸發深度推理的提示（例如，高熱忱、自我反思代碼），非串流呼叫可能花費很長時間 — 有時甚至數分鐘 — 沒有任何可見反饋。**在試驗複雜提示時請使用串流功能**，既可見模型運作，也避免誤以為請求超時。
>
> **注意：瀏覽器要求** — 串流功能使用 Fetch Streams API（`response.body.getReader()`），需完整瀏覽器支援（Chrome、Edge、Firefox、Safari）。 VS Code 內建的 Simple Browser 不支援 ReadableStream API，故無法串流。使用 Simple Browser 時，非串流按鈕仍正常，只有串流按鈕受影響。請用外部瀏覽器開啟 `http://localhost:8083` 以獲得完整體驗。

### 低熱忱 vs 高熱忱

用低熱忱提問簡單問題，如「15% 的 200 是多少？」將獲得即時且直接的答案。現在用高熱忱提問複雜問題，如「設計一個高流量 API 的快取策略」。點擊 **🔴 串流回應（即時）**，觀看模型詳細推理逐字元顯現。是同一模型，同一問題結構 — 但提示決定了思考深度。

### 任務執行（工具前置語）

多步工作流受益於事先規劃與過程敘述。模型會概述要做的事，敘述每步，最後總結結果。

### 自我反思代碼

嘗試「建立一個電郵驗證服務」。模型不只是生成代碼並停下，而是生成、依品質標準評估、找出弱點並改進。你會看到它反覆迭代，直至代碼符合生產標準。

### 結構化分析

程式碼審查需一致評估框架。模型使用固定類別（正確性、實踐、效能、安全性）及嚴重程度分析代碼。

### 多輪對話

提問「什麼是 Spring Boot？」，緊接著再問「舉例說明」。模型記得第一個問題，並給你一個具體的 Spring Boot 範例。沒有記憶，第二個問題太模糊。

### 逐步推理

挑選數學問題，用逐步推理和低熱忱測試。低熱忱快速給答案，但不透明。逐步推理則展示所有計算和判斷。

### 受限輸出

需要特定格式或字數時，此範式強制嚴格遵守。試著生成精確 100 字的列點摘要。

## 你真正學到的

**推理努力改變一切**

GPT-5.2 讓你透過提示控制計算努力。低努力意味快速回應、最少探索。高努力則意謂模型花時間深入思考。你在學習根據任務複雜度匹配努力 — 簡單問題不要浪費時間，複雜決策也不要急促。

**結構引導行為**

注意提示中的 XML 標籤？它們不是裝飾品。模型比自由文本更可靠地遵循結構化指令。需要多步驟流程或複雜邏輯時，結構有助模型追蹤進度和下一步。下圖將一個結構良好的提示拆解，顯示 `<system>`, `<instructions>`, `<context>`, `<user-input>`, `<constraints>` 等標籤如何將指令劃分清晰區塊。

<img src="../../../translated_images/zh-MO/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*結構良好提示的解剖，有清晰區段和 XML 風格組織*

**品質來自自我評估**

自我反思範式透過明確品質標準工作。你不再希望模型「表現正確」，而是直接告訴它「正確」定義為：邏輯正確、錯誤處理、效能、安全。這使模型能評估自身輸出並改進。代碼生成從抽獎變成流程。

**上下文有限**

多輪對話通過每次請求包含訊息歷史來運作。但有字元限制 — 每個模型有最大字元數。隨對話成長，你將學習策略保持相關上下文而不超限。本模組展示記憶機制；稍後你會知道何時總結、忘記及檢索。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導覽：** [← 上一個：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一個：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應被視為權威來源。對於重要資訊，建議採用專業人工作翻譯。我們對因使用本翻譯而引致之任何誤解或誤譯概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->