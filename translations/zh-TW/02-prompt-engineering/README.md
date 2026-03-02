# Module 02: 使用 GPT-5.2 進行提示工程

## 內容目錄

- [影片導覽](../../../02-prompt-engineering)
- [您將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [了解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少量示例提示](../../../02-prompt-engineering)
  - [思維鏈](../../../02-prompt-engineering)
  - [角色導向提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [執行應用程式](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低熱忱 vs 高熱忱](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思程式碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多回合聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [您真正學到的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 影片導覽

觀看此現場講解影片，了解如何開始本模組：

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="與 LangChain4j 的提示工程 - 現場講解" width="800"/></a>

## 您將學到什麼

下圖提供了本模組關鍵主題與技能的概覽——從提示優化技術到您將使用的逐步工作流程。

<img src="../../../translated_images/zh-TW/what-youll-learn.c68269ac048503b2.webp" alt="您將學到什麼" width="800"/>

在之前的模組中，您探索了與 GitHub 模型的基本 LangChain4j 互動，並見識了記憶如何透過 Azure OpenAI 啟用對話式 AI。現在，我們將專注於您如何提問——也就是提示本身——利用 Azure OpenAI 的 GPT-5.2。您設計的提示結構會大幅影響回應品質。我們從提示基本技巧回顧開始，接著探討八種進階模式，充分利用 GPT-5.2 的能力。

我們使用 GPT-5.2 是因為它引入了推理控制——您可以告訴模型在回答前要思考多少。這讓不同的提示策略更加明顯，幫助您了解何時該使用每種方法。此外，與 GitHub 模型相比，Azure 對 GPT-5.2 的速率限制較少，我們也將因此受益。

## 先決條件

- 完成模組 01（已部署 Azure OpenAI 資源）
- 根目錄下有 `.env` 檔案，包含 Azure 憑證（由模組 01 中的 `azd up` 產生）

> **注意：** 如果尚未完成模組 01，請先依照那裡的部署說明操作。

## 了解提示工程

提示工程的核心，是模糊指令與精確指令之間的差異，以下圖說明。

<img src="../../../translated_images/zh-TW/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程就是設計輸入文字以穩定獲得所需結果。它不只是問問題——而是構建請求，讓模型能精準了解您要什麼，並該怎麼回覆。

想像您在給同事指示。「修復錯誤」很模糊。「在 UserService.java 第 45 行針對 null 指標例外新增空值檢查」則很具體。語言模型的運作也是如此——具體與結構同樣重要。

下圖展示了 LangChain4j 如何融入這個流程——透過 SystemMessage 和 UserMessage 建構塊將您的提示模式與模型連接起來。

<img src="../../../translated_images/zh-TW/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 如何融入" width="800"/>

LangChain4j 提供基礎架構——模型連接、記憶體及訊息類型，而提示模式則是您透過該架構傳送的精心結構化文字。關鍵建構元是 `SystemMessage`（設定 AI 行為與角色）與 `UserMessage`（承載您的實際請求）。

## 提示工程基礎

以下五種核心技巧構成有效提示工程的基石。每一種都涵蓋您與語言模型溝通的不同面向。

<img src="../../../translated_images/zh-TW/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式概覽" width="800"/>

在深入本模組的進階模式前，讓我們回顧五個基礎提示技巧。這些是每個提示工程師都應了解的基礎。若您已完成 [快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，便見過它們的實作——以下為其背後的概念框架。

### 零次提示

最簡單的方式：直接給模型指令，沒有示例。模型完全依靠訓練來理解與執行任務。適合行為明確的簡單請求。

<img src="../../../translated_images/zh-TW/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次提示" width="800"/>

*沒有示例的直接指令——模型僅從指令本身推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應:「正面」
```
  
**適用時機：** 簡單分類、直接提問、翻譯，或模型能在無額外指引下處理的任何任務。

### 少量示例提示

提供示例，示範您希望模型遵循的模式。模型學習示例的輸入輸出格式，並應用於新輸入。此法大幅提升格式或行為不明顯任務的一致性。

<img src="../../../translated_images/zh-TW/few-shot-prompting.9d9eace1da88989a.webp" alt="少量示例提示" width="800"/>

*從示例學習——模型辨識模式，且套用於新輸入*

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
  
**適用時機：** 客製分類、一致格式化、領域特定任務，或零次提示結果不一致時。

### 思維鏈

要求模型逐步展現推理過程。模型不直接跳到答案，而是明確拆解問題並逐步處理。此法提高數學、邏輯與多步推理任務的準確性。

<img src="../../../translated_images/zh-TW/chain-of-thought.5cff6630e2657e2a.webp" alt="思維鏈提示" width="800"/>

*逐步推理——將複雜問題拆成清楚邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 顆蘋果
```
  
**適用時機：** 數學題、邏輯謎題、除錯，或者任何展現推理過程能提升準確性與可信度的任務。

### 角色導向提示

在提問前設定 AI 的角色或身份。這提供上下文，塑造回答的語氣、深度與焦點。例如「軟體架構師」與「初級開發者」或「資安審計員」會給不同建議。

<img src="../../../translated_images/zh-TW/role-based-prompting.a806e1a73de6e3a4.webp" alt="角色導向提示" width="800"/>

*設定上下文與身份——同一問題因角色不同而得到不同回答*

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
  
**適用時機：** 程式碼審查、教學、領域特定分析，或您需要依據特定專業層次或觀點量身訂做回應時。

### 提示模板

建立可重用提示，含變數占位符。無需每次重寫提示，只要定義模板並填入不同值即可。LangChain4j 的 `PromptTemplate` 類以 `{{variable}}` 語法協助此工作。

<img src="../../../translated_images/zh-TW/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*含變數占位的可重用提示——一個模板，多次使用*

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
  
**適用時機：** 多次查詢不同輸入、批次處理、建立可重用 AI 工作流程，或任何提示結構固定但資料會變的場景。

---

這五種基礎技巧為您提供大部分提示任務的強大工具組。本模組餘下內容將基於此，融入 GPT-5.2 的推理控制、自我評估及結構化輸出功能，展開 **八種進階模式**。

## 進階模式

基礎技巧說明完畢，我們轉向本模組獨有的八種進階模式。不是所有問題都適用相同方法。有些問題需迅速回答，有些需要深入思考。有些需展現明確推理，有些只要結果。以下每種模式都為不同情境優化，而 GPT-5.2 的推理控制更突顯出差異。

<img src="../../../translated_images/zh-TW/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示工程模式" width="800"/>

*八種提示工程模式及其使用情境概覽*

GPT-5.2 為這些模式增添一個新維度：*推理控制*。下面滑桿顯示您如何調整模型的思考深度——從快速直接的答案到深入徹底的分析。

<img src="../../../translated_images/zh-TW/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 推理控制" width="800"/>

*GPT-5.2 的推理控制讓您指定模型思考量——從迅速直接回答到深入探索皆可*

**低熱忱（快速且聚焦）** - 適合簡單問題，需快速且直接答案。模型進行最少推理——最多兩步。用於計算、查詢或直接問題。

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
  
> 💡 **用 GitHub Copilot 探索：** 開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 並詢問：  
> - 「低熱忱與高熱忱提示模式有何差異？」  
> - 「提示中的 XML 標籤如何幫助結構化 AI 回答？」  
> - 「什麼情況應使用自我反思模式，什麼情況用直接指令？」

**高熱忱（深入且徹底）** - 適合複雜問題，需全面分析。模型徹底探索並展示詳盡推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**任務執行（逐步進度）** - 適用於多步工作流程。模型提供事先計劃，工作時口述每步，最後再彙整。用於遷移、實作或任意多步程序。

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
  
思維鏈提示明確要求模型展現推理流程，提升複雜任務精確度。逐步拆解幫助人類與 AI 了解邏輯。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 詢問此模式：  
> - 「我怎麼為長時間執行操作調整任務執行模式？」  
> - 「生產環境應用中，工具前言該如何結構化？」  
> - 「如何在 UI 中擷取並顯示中間進度更新？」

下圖描繪此 計劃 → 執行 → 彙整 工作流程。

<img src="../../../translated_images/zh-TW/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*多步任務的計劃 → 執行 → 彙整工作流程*

**自我反思程式碼** - 用於生成生產級程式碼。模型產生符合生產標準且具備適當錯誤處理的程式碼。建置新功能或服務時使用。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
下圖展示此反覆改進迴圈——產生、評估、找出弱點並改良，直至滿足生產標準。

<img src="../../../translated_images/zh-TW/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*反覆改進迴圈——產生、評估、識別問題、改良、重複*

**結構化分析** - 用於一致性評價。模型依據固定框架（正確性、慣例、效能、安全性、維護性）審查程式碼。適用程式碼審查或品質評估。

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
  
> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 詢問結構化分析：  
> - 「如何客製不同類型程式碼審查的分析框架？」  
> - 「程式化解析並利用結構化輸出的最佳作法？」  
> - 「如何確保不同審查回合間的嚴重性等級一致？」

下圖展示此結構化框架如何將程式碼審查組織成一致類別並附嚴重程度。

<img src="../../../translated_images/zh-TW/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*一致程式碼審查的框架與嚴重性分類*

**多回合聊天** - 適用需要上下文的對話。模型記住先前訊息並在基礎上回應。用於互動式協助會話或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
下圖視覺化對話上下文如何累積以及它與模型代幣限制的關係。

<img src="../../../translated_images/zh-TW/context-memory.dff30ad9fa78832a.webp" alt="上下文記憶" width="800"/>

*多回合對話中上下文如何累積，直至達到代幣限制*
**逐步推理** - 適用於需要明確邏輯的問題。模型對每個步驟進行明確推理。用於數學題、邏輯謎題，或當你需要理解思考過程時。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

下圖展示模型如何將問題拆解成明確、有編號的邏輯步驟。

<img src="../../../translated_images/zh-TW/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*將問題拆解為明確的邏輯步驟*

**受限輸出** - 適用於有特定格式要求的回應。模型嚴格遵守格式與長度規則。用於摘要或需要精確輸出結構的情況。

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

下圖顯示如何藉由約束，讓模型產出嚴格符合格式與長度要求的內容。

<img src="../../../translated_images/zh-TW/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*強制特定格式、長度及結構要求*

## 執行應用程式

**驗證部署：**

確保根目錄有 `.env` 檔案，並包含 Azure 憑證（在模組 01 中建立）。請在模組資料夾 (`02-prompt-engineering/`) 執行：

**Bash:**  
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```
  
**啟動應用程式：**

> **注意：** 如果你已從根目錄執行過 `./start-all.sh` （如模組 01 所述），這個模組已在 8083 埠執行。可跳過以下啟動指令，直接訪問 http://localhost:8083。

**選項 1：使用 Spring Boot Dashboard（建議 VS Code 使用者）**

開發容器包含 Spring Boot Dashboard 擴充功能，提供視覺化介面管理所有 Spring Boot 應用程式。可在 VS Code 左邊活動欄找到 Spring Boot 圖示。

在 Spring Boot Dashboard 中，你可以：  
- 查看工作區所有可用的 Spring Boot 應用程式  
- 一鍵啟動或停止應用程式  
- 實時查看應用日誌  
- 監控應用狀態  

只需點擊 "prompt-engineering" 旁的播放按鈕，即可啟動此模組，或同時啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot Dashboard — 從一處啟動、停止與監控所有模組*

**選項 2：使用 shell 腳本**

啟動所有網頁應用（模組 01-04）：

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
  
兩個腳本會自動從根目錄的 `.env` 載入環境變數，且如不存在 JAR 檔會自動編譯。

> **注意：** 若你偏好先手動編譯所有模組，再啟動：  
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
  
以瀏覽器開啟 http://localhost:8083 。

**停止：**

**Bash:**  
```bash
./stop.sh  # 僅此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # 僅此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```
  
## 應用截圖

這是 prompt engineering 模組的主介面，能讓你並排實驗所有八種模式。

<img src="../../../translated_images/zh-TW/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主控台顯示全部 8 種 prompt engineering 模式及其特性與適用情境*

## 探索各種模式

網頁介面允許你嘗試不同提示策略。每個模式解決不同問題—嘗試看看何時適合使用哪種方法。

> **注意：串流與非串流** — 每個模式頁面有兩個按鈕：**🔴 串流回應（即時）** 和 **非串流**。串流採用 Server-Sent Events (SSE)，在模型生成 token 時即時顯示，讓你立即看到進度。非串流則會等待整段回應完成後才顯示。對於需要深度推理的提示（如 高渴望、自我反思代碼），非串流可能非常耗時，可能需要數分鐘且無任何反饋。**試驗複雜提示時請使用串流，讓你看到模型運作，避免誤認請求逾時。**  
>  
> **注意：瀏覽器需求** — 串流功能使用 Fetch Streams API（`response.body.getReader()`），需完整瀏覽器（Chrome、Edge、Firefox、Safari）。不支援 VS Code 內建的 Simple Browser，因其 WebView 不支援 ReadableStream API。使用 Simple Browser 時，非串流按鈕正常，只有串流按鈕受限。建議用外部瀏覽器開啟 `http://localhost:8083` 以獲得完整體驗。

### 低渴望 vs 高渴望

用低渴望問簡單問題，例如「200 的 15% 是多少？」你會即時收到直接答案。再用高渴望問複雜問題，例如「設計高流量 API 的快取策略」。點擊 **🔴 串流回應（即時）**，觀看模型逐字展現詳細推理。同一模型，同一問題結構，唯提示決定思考深度。

### 任務執行（工具前置指令）

多步驟工作流程適合先規劃並敘述進程。模型會列出將採取的步驟，逐步敘述，最後彙整結果。

### 自我反思思考代碼

試試「建立電子郵件驗證服務」。模型不只是生成代碼並停下，而是產出、對照品質標準評估、找出不足並改進。你會看到它反覆迭代直到代碼符合生產標準。

### 結構化分析

程式碼審查需要一致評估框架。模型會用固定分類（正確性、慣例、效能、安全）進行分析，並設置嚴重度等級。

### 多輪對話

問「什麼是 Spring Boot？」後立即追問「給我範例」。模型會記住前一問題，提供專門的 Spring Boot 範例。沒有記憶，第二題太模糊。

### 逐步推理

選一道數學題，分別用逐步推理和低渴望嘗試。低渴望只給答案，快速但不透明。逐步推理顯示每個計算和決策過程。

### 受限輸出

需要特定格式或字數，此模式強制嚴格遵守。試試用分點方式產生正好 100 字的摘要。

## 你真正學到的是什麼

**推理努力決定一切**

GPT-5.2 讓你透過提示控制計算努力程度。低努力輸出快速且探索少。高努力意味模型花時間深度思考。你學會調整努力與任務複雜度匹配—別浪費時間在簡單問題，但也別急於複雜決策。

**結構引導行為**

注意提示中的 XML 標籤？它們非裝飾用。模型比起自由文本，更容易遵循結構化指令。多步驟流程或複雜邏輯時，結構幫助模型追蹤目前狀態與接下來步驟。下圖拆解良好結構化的提示，說明 `<system>`、`<instructions>`、`<context>`、`<user-input>` 與 `<constraints>` 如何將指令分門別類。

<img src="../../../translated_images/zh-TW/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*具明確區段與 XML 式組織的良好結構化提示構造*

**透過自我評估提升品質**

自我反思模式明確傳達品質標準。別只是希望模型「做對」，而是告訴它「對」的定義：正確邏輯、錯誤處理、效能、安全。模型能自評並改善輸出，讓程式碼生成從抽獎變為流程。

**上下文有限**

多輪對話透過每次請求夾帶訊息歷史，但有限制—模型有最大 token 數。對話逐漸增長，需要策略保留相關上下文且不超過限制。本模組示範記憶運作，日後你會學會何時摘要、何時忘記、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導航：** [← 上一章：模組 01 - 簡介](../01-introduction/README.md) | [回主目錄](../README.md) | [下一章：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們盡力確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應視為權威來源。對於重要資訊，建議使用專業人工翻譯。我們對於因使用本翻譯而產生之任何誤解或誤譯不負任何責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->