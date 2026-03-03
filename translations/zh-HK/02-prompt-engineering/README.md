# 模組 02：使用 GPT-5.2 進行提示工程

## 目錄

- [影片導覽](../../../02-prompt-engineering)
- [你將學到的內容](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [了解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次示範提示](../../../02-prompt-engineering)
  - [少量示範提示](../../../02-prompt-engineering)
  - [思維鏈](../../../02-prompt-engineering)
  - [角色導向提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [執行應用程式](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低與高積極度](../../../02-prompt-engineering)
  - [任務執行（工具序言）](../../../02-prompt-engineering)
  - [自我反思程式碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 影片導覽

觀看此直播課程，說明如何開始此模組：

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="LangChain4j 提示工程 - 直播課程" width="800"/></a>

## 你將學到的內容

以下圖解概述了你在本模組中將發展的主要主題和技能 — 從提示細化技術到你將遵循的逐步工作流程。

<img src="../../../translated_images/zh-HK/what-youll-learn.c68269ac048503b2.webp" alt="你將學到的內容" width="800"/>

在之前的模組中，你探索了與 GitHub 模型的基本 LangChain4j 互動，並了解記憶如何使 Azure OpenAI 的對話式 AI 成為可能。現在我們將聚焦於如何提出問題—即提示本身—使用 Azure OpenAI 的 GPT-5.2。你如何結構提示會極大地影響你獲得的回應品質。我們先回顧基本提示技術，然後進入八種利用 GPT-5.2 全面能力的進階模式。

我們選擇 GPT-5.2 是因為它引入了推理控制——你可以告訴模型在回答前要思考多少。這使不同提示策略更加明顯，幫助你理解何時使用每種方法。我們還能受益於 GPT-5.2 在 Azure 上比 GitHub 模型更少的速率限制。

## 先決條件

- 完成模組 01（已部署 Azure OpenAI 資源）
- 在根目錄存在 `.env` 檔案並含有 Azure 認證（由模組 01 的 `azd up` 建立）

> **注意：** 若尚未完成模組 01，請先按照該模組中的部署說明進行。

## 了解提示工程

本質上，提示工程是抽象指令與精確指令之間的差異，以下比較說明了這點。

<img src="../../../translated_images/zh-HK/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文本，使你能持續獲得所需結果。這不僅是提問，更是構造請求，使模型精確理解你想要什麼及如何回應。

想像你在給同事下達指令。「修正錯誤」很模糊。「修正 UserService.java 第 45 行的 null 指針異常，新增 null 檢查」則很具體。語言模型運作亦然 — 明確性與結構同等重要。

下圖顯示 LangChain4j 如何融入此流程—透過 SystemMessage 與 UserMessage 構建塊，將你的提示模式連接到模型。

<img src="../../../translated_images/zh-HK/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的作用" width="800"/>

LangChain4j 提供基礎架構—模型連接、記憶與訊息類型—而提示模式則是透過這些基礎架構傳遞的精心結構化文本。關鍵的建構塊是 `SystemMessage`（設定 AI 行為和角色）與 `UserMessage`（承載你的實際請求）。

## 提示工程基礎

以下五種核心技術構成有效提示工程的基礎。每種技巧都處理你與語言模型溝通的不同面向。

<img src="../../../translated_images/zh-HK/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式概覽" width="800"/>

在深入本模組的進階模式之前，先回顧五種基礎提示技術。這是每位提示工程師應掌握的基石。如果你已閱覽過[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，你應該對這些有所了解 — 以下為其概念框架。

### 零次示範提示

最簡單的方法：給模型直接指令，無需範例。模型完全依訓練去理解與執行任務。適用於期望行為明確的簡單請求。

<img src="../../../translated_images/zh-HK/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次示範提示" width="800"/>

*沒有範例的直接指令 — 模型僅從指令推理任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應:「正面」
```

**使用時機：** 簡單分類、直接提問、翻譯或任何模型能無額外指導完成的任務。

### 少量示範提示

提供示例以展示期望模型遵循的模式。模型從範例學習輸入輸出格式並應用於新輸入。對於目標格式或行為不明顯的任務，大幅提升一致性。

<img src="../../../translated_images/zh-HK/few-shot-prompting.9d9eace1da88989a.webp" alt="少量示範提示" width="800"/>

*從示例學習 — 模型識別模式並套用至新輸入*

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

**使用時機：** 自訂分類、一致格式、領域特定任務，或零次示範表現不穩定時。

### 思維鏈

要求模型逐步展示其推理過程。模型不直接給答案，而是分解問題逐步說明。提升數學、邏輯與多步推理任務的準確度。

<img src="../../../translated_images/zh-HK/chain-of-thought.5cff6630e2657e2a.webp" alt="思維鏈提示" width="800"/>

*逐步推理 — 將複雜問題分解成明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**使用時機：** 數學題、邏輯謎題、除錯，或任何展示推理過程能提升準確度與信任的任務。

### 角色導向提示

在提問前設定 AI 的角色或身份。此上下文塑造回應的語氣、深度與焦點。「軟件架構師」的建議與「初級開發者」或「安全稽核員」截然不同。

<img src="../../../translated_images/zh-HK/role-based-prompting.a806e1a73de6e3a4.webp" alt="角色導向提示" width="800"/>

*設定上下文與角色 — 相同問題因角色不同而獲得不同回答*

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

**使用時機：** 代碼審查、教學、領域特定分析，或需要根據專業層次或視角定制回應時。

### 提示模板

創建具可變佔位符的可重用提示。無須每次撰寫新提示，定義模板後只需填入不同數值。LangChain4j 的 `PromptTemplate` 類透過 `{{variable}}` 語法輕鬆實現。

<img src="../../../translated_images/zh-HK/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*具可變佔位符的可重用提示 — 一個模板，多種使用*

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

**使用時機：** 需重複查詢且輸入不同、批量處理、構建可重用 AI 工作流，或任何提示結構不變但數據改變的場景。

---

這五個基礎給你建立了一套適用於大多數提示任務的工具組。本模組餘下部分，基於此構建了**八種進階模式**，充分利用 GPT-5.2 的推理控制、自我評估與結構化輸出功能。

## 進階模式

基礎理論講解完畢，讓我們來看看本模組獨特的八種進階模式。並非所有問題都適用同一策略。有些問題需要快速回應，有些需要深度思考。有些需展現推理過程，有些只求結果。以下每種模式都針對不同場景最佳化——GPT-5.2 的推理控制讓這些差異更為明顯。

<img src="../../../translated_images/zh-HK/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示工程模式" width="800"/>

*八種提示工程模式及其使用場景概覽*

GPT-5.2 增加了另一維度：*推理控制*。下方滑桿展示你如何調整模型的思考努力程度 — 從快速直接回答到深度詳盡分析。

<img src="../../../translated_images/zh-HK/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 推理控制" width="800"/>

*GPT-5.2 的推理控制讓你指定模型要思考多少 — 從快速直接答覆到深度探索*

**低積極度（快速與聚焦）** — 適用於你想要快速且直接回答的簡單問題。模型只進行最少推理 - 最多兩步。用於計算、查詢或直白問題。

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

> 💡 **用 GitHub Copilot 探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 並提問：
> - 「低積極度和高積極度提示模式有何不同？」
> - 「提示中的 XML 標籤如何幫助結構 AI 回應？」
> - 「何時應使用自我反思模式而非直接指令？」

**高積極度（深入與周全）** — 適用於你想要全面分析的複雜問題。模型深入探索並呈現詳細推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** — 適用於多步工作流。模型事先提供計劃，邊工作邊敘述步驟，最後做總結。用於遷移、實作或任何多步驟流程。

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

思維鏈提示明確要求模型展示推理過程，提升複雜任務的準確度。逐步分解有助於人類與 AI 理解邏輯。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問此模式：
> - 「如何改編任務執行模式以支援長時間運行操作？」
> - 「生產應用中工具序言結構的最佳實踐是什麼？」
> - 「如何捕捉並在使用者介面中顯示中間進度更新？」

下圖說明此計劃 → 執行 → 總結的工作流程。

<img src="../../../translated_images/zh-HK/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*多步任務的計劃 → 執行 → 總結工作流程*

**自我反思程式碼** — 用於產出生產品質程式碼。模型根據生產標準生成程式碼，具備妥善錯誤處理。用於構建新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

下圖展示此迭代改進循環 — 生成、評估、識別弱點，反覆修正直至達生產標準。

<img src="../../../translated_images/zh-HK/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改進循環 — 生成、評估、識別問題、改進、重複*

**結構化分析** — 用於一致性評估。模型依固定框架檢視程式碼（正確性、實踐、效能、安全、可維護性）。適用於代碼審查或品質評估。

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

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問結構化分析：
> - 「如何為不同類型的代碼審查自訂分析框架？」
> - 「以程式化方式解析並操作結構化輸出的最佳方法？」
> - 「如何確保不同審查會議間嚴重程度的一致？」

下圖展示如何用此結構化框架將代碼審查組織成一致分類並標明嚴重等級。

<img src="../../../translated_images/zh-HK/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*具嚴重程度等級的一致性代碼審查框架*

**多輪對話** — 用於需要上下文的對話。模型記憶先前訊息並基於該上下文回應。用於互動幫助或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

下圖視覺化對話上下文如何隨多輪交流累積，以及其與模型代幣限制的關係。

<img src="../../../translated_images/zh-HK/context-memory.dff30ad9fa78832a.webp" alt="上下文記憶" width="800"/>

*對話上下文隨多輪交流累積，直到接近代幣限制*
**逐步推理** - 適用於需要清晰邏輯的問題。模型會為每個步驟展示明確的推理。用於數學題、邏輯謎題，或當你需要了解思考過程時。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

下圖展示模型如何將問題拆解為明確編號的邏輯步驟。

<img src="../../../translated_images/zh-HK/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*將問題拆解為明確的邏輯步驟*

**受限輸出** - 適用於有特定格式需求的回應。模型嚴格遵守格式和長度規則。用於摘要或需要精確輸出結構的情況。

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

下圖展示約束如何引導模型產出嚴格符合格式和長度要求的結果。

<img src="../../../translated_images/zh-HK/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*強制執行特定格式、長度和結構要求*

## 運行應用程式

**驗證部署：**

確保根目錄存在 `.env` 文件且內含 Azure 憑證（在模組 01 中建立）。從模組目錄（`02-prompt-engineering/`）執行：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已經從根目錄使用 `./start-all.sh` 啟動所有應用程式（模組 01 中說明），該模組已在 8083 埠運行。你可以跳過以下啟動指令，直接訪問 http://localhost:8083。

**選項 1：使用 Spring Boot 儀表板（建議 VS Code 使用者）**

開發容器包含 Spring Boot 儀表板擴充功能，提供管理所有 Spring Boot 應用程式的可視介面。你可以在 VS Code 左側活動列中找到（尋找 Spring Boot 圖示）。

在 Spring Boot 儀表板中，你可以：
- 查看工作區中所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 實時查看應用程式日誌
- 監控應用程式狀態

只需點擊 "prompt-engineering" 旁的播放按鈕啟動該模組，或一次啟動全部模組。

<img src="../../../translated_images/zh-HK/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot 儀表板 — 從一處啟動、停止及監控所有模組*

**選項 2：使用 shell 腳本**

啟動所有 Web 應用程式（模組 01-04）：

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

兩個腳本會自動從根目錄的 `.env` 文件載入環境變數，且如果 JAR 文件不存在，會自動構建。

> **注意：** 如果你偏好先手動構建所有模組，然後再啟動：
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

在瀏覽器打開 http://localhost:8083 。

**停止應用程式：**

**Bash:**
```bash
./stop.sh  # 僅此模組
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

下面是 prompt engineering 模組的主介面，你可以在這裡並排嘗試全部八種模式。

<img src="../../../translated_images/zh-HK/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主儀表板顯示全部 8 種 prompt engineering 模式及其特性和使用案例*

## 探索各種模式

網頁介面讓你嘗試不同的提示策略。每個模式解決不同問題 — 試試看，了解各種方法何時最有效。

> **注意：串流與非串流** — 每個模式頁面都有兩個按鈕：**🔴 串流回應（實時）** 及 **非串流** 選項。串流利用 Server-Sent Events (SSE)，在模型生成時立即呈現標記，因此你能即時看到進度。非串流則會等整個回答產生完才顯示。對於會觸發深度推理的提示（如高積極度、具自我反思的程式碼），非串流呼叫可能耗時甚久，甚至數分鐘而無任何可見回饋。**嘗試複雜提示時建議使用串流，這樣可以看到模型運作，避免誤以為請求超時。**
>
> **注意：瀏覽器需求** — 串流功能使用 Fetch Streams API (`response.body.getReader()`)，需完整瀏覽器（Chrome、Edge、Firefox、Safari）。VS Code 內建的 Simple Browser 不支援 ReadableStream API，故不支援串流功能。若使用 Simple Browser，非串流按鈕仍正常工作，僅串流按鈕受影響。建議使用外部瀏覽器在 `http://localhost:8083` 享用完整體驗。

### 低積極度 vs 高積極度

用低積極度問一個簡單問題，例如「200 的 15% 是多少？」，會快速得到直接答案。再用高積極度問複雜問題，例如「設計一個高流量 API 的快取策略」，點擊 **🔴 串流回應（實時）**，看模型逐字生成詳細推理。相同模型，相同問題結構 — 差別在輸入提示告訴模型思考的深度。

### 任務執行（工具前置提示）

多步流程需要事先計劃並逐步解說。模型會先概述計劃，敘述每一步執行，最後總結結果。

### 自我反思程式碼

試試「建立一個電子郵件驗證服務」。模型不只產生程式碼然後停止，而是生成、依品質標準評估、找出弱點並改進。你將看到它持續迭代直到程式碼符合生產標準。

### 結構化分析

程式碼檢視需一致的評估框架。模型會以固定分類（正確性、實務、效能、安全）分析程式碼，並給予嚴重度等級。

### 多輪對話

問「什麼是 Spring Boot？」接著馬上追問「示範一個範例」。模型會記住第一題，並給你專門針對 Spring Boot 的範例。沒記憶的話，第二題就太模糊了。

### 逐步推理

選一個數學問題，分別用逐步推理與低積極度嘗試。低積極度只給答案 — 快速但不透明。逐步推理會展示每個計算與決策。

### 受限輸出

當你需要特定格式或字數限制時，該模式嚴格執行規則。試生成一個正好 100 字的列點摘要。

## 你真正學會的是什麼

**推理深度改變一切**

GPT-5.2 讓你可透過提示控制計算努力程度。低努力意味快速回應且探索有限。高努力則投入更多時間深入思考。你學會將努力度配合任務複雜性 — 簡單題勿浪費時間，複雜決策也別匆忙。

**結構引導行為**

注意提示中的 XML 標籤？它們非裝飾用。模型更可靠地遵照結構化指示執行任務。多步程序或複雜邏輯時，結構有助模型追蹤進度及下一步行動。下圖解析一個良好結構化提示，展示 `<system>`、`<instructions>`、`<context>`、`<user-input>`、`<constraints>` 如何將指令分段明確整理。

<img src="../../../translated_images/zh-HK/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*精良結構化提示的組成，清晰分段與 XML 風格組織*

**品質來自自我評估**

自我反思模式藉由明確品質標準運作。不是寄望模型「做對」，而是告訴它「對」的定義：正確邏輯、錯誤處理、效能、安全。模型可自我評估輸出並改進。這讓程式碼生成不是賭運氣，而是流程。

**上下文有限**

多輪對話透過在每次請求中包含訊息歷史實現。但有上限 — 每個模型皆有最大代幣數。隨著對話成長，你必須運用策略保留重要上下文且不超限。本模組展示記憶運作；未來你會學習何時摘要、何時遺忘、何時回溯。

## 下一步

**下一模組：** [03-rag - RAG (檢索增強生成)](../03-rag/README.md)

---

**導航：** [← 上一頁：模組 01 - 簡介](../01-introduction/README.md) | [回主頁](../README.md) | [下一頁：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於關鍵資訊，建議使用專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->