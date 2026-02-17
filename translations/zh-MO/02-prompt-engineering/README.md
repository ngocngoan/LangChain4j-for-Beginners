# Module 02: 使用 GPT-5.2 的提示工程學

## 目錄

- [你將學到的內容](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程學](../../../02-prompt-engineering)
- [提示工程學基礎](../../../02-prompt-engineering)
  - [零次提示（Zero-Shot）](../../../02-prompt-engineering)
  - [少次提示（Few-Shot）](../../../02-prompt-engineering)
  - [思維鏈（Chain of Thought）](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示範本](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索這些模式](../../../02-prompt-engineering)
  - [低熱忱 vs 高熱忱](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思程式碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的東西](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你將學到的內容

<img src="../../../translated_images/zh-MO/what-youll-learn.c68269ac048503b2.webp" alt="你將學到的內容" width="800"/>

在上一個模塊中，你已了解記憶如何讓對話型 AI 運作，並使用 GitHub 模型進行基本互動。現在我們將聚焦於你如何提問——也就是提示本身——利用 Azure OpenAI 的 GPT-5.2。你如何組織提示會極大影響輸出回應的品質。我們先回顧基本的提示技術，然後進入八種利用 GPT-5.2 全面能力的進階模式。

我們使用 GPT-5.2 是因為它引入了推理控制——你可以告訴模型在回答前應該思考多少。這讓不同的提示策略更為明顯，也有助你了解何時使用哪種方法。此外，Azure 對 GPT-5.2 的頻率限制比 GitHub 模型更寬鬆，我們也將受益於此。

## 先決條件

- 完成模塊 01（已部署 Azure OpenAI 資源）
- 根目錄中有 `.env` 檔案並包含 Azure 認證（模塊 01 由 `azd up` 建立）

> **注意：** 如果你還沒完成模塊 01，請先按照那裡的部署說明進行。

## 理解提示工程學

<img src="../../../translated_images/zh-MO/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="提示工程學是什麼？" width="800"/>

提示工程學是設計輸入文本以穩定得到你所需結果的技術。它不只是問問題——而是結構化請求，讓模型精確理解你想要什麼以及如何交付。

想像你在給同事下指令。「修復錯誤」很模糊。「在 UserService.java 第 45 行透過加入 null 檢查修復 null 指標異常」就很具體。語言模型運作也是如此——具體和結構很重要。

<img src="../../../translated_images/zh-MO/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 如何契合" width="800"/>

LangChain4j 提供基礎設施——模型連接、記憶和訊息類型——而提示模式只是你透過該基礎設施發送的精心結構化文本。關鍵構件是 `SystemMessage`（設定 AI 行為與角色）及 `UserMessage`（傳達你的實際請求）。

## 提示工程學基礎

<img src="../../../translated_images/zh-MO/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式總覽" width="800"/>

在深入本模塊的進階模式之前，讓我們先複習五種基礎提示技術。這些是每個提示工程師必備的構建基礎。若你已完成[快速入門模塊](../00-quick-start/README.md#2-prompt-patterns)，你應該對這些有實際了解——下面是它們背後的概念框架。

### 零次提示（Zero-Shot Prompting）

最簡單的方法：直接給模型一個指令，沒有提供範例。模型完全依賴訓練學到的知識來理解並執行任務。適用於期望行為明確的簡單請求。

<img src="../../../translated_images/zh-MO/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次提示" width="800"/>

*無範例的直接指令——模型僅從指令自行推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應:「正面」
```

**使用時機：** 簡單分類、直接問題、翻譯，或任何模型無須額外指引即可處理的任務。

### 少次提示（Few-Shot Prompting）

提供範例展示你希望模型遵循的模式。模型從範例學習預期的輸入-輸出格式，並將其應用於新輸入。對於所需格式或行為不明顯的任務，能顯著提升一致性。

<img src="../../../translated_images/zh-MO/few-shot-prompting.9d9eace1da88989a.webp" alt="少次提示" width="800"/>

*從範例中學習——模型辨識模式並套用於新輸入*

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

**使用時機：** 自訂分類、一致格式化、特定領域任務，或零次提示結果不穩定的狀況。

### 思維鏈（Chain of Thought）

要求模型逐步展示推理過程。模型不直接給答案，而是詳細拆解問題並明確執行每一步。提高數學、邏輯和多階段推理任務的準確度。

<img src="../../../translated_images/zh-MO/chain-of-thought.5cff6630e2657e2a.webp" alt="思維鏈提示" width="800"/>

*逐步推理——將複雜問題拆解為明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 減 8 等於 7，然後 7 加 12 等於 19 個蘋果
```

**使用時機：** 數學問題、邏輯謎題、除錯，或任何需顯示推理過程以提升準確性和信任度的任務。

### 基於角色的提示

在提問前先設定 AI 的身份或角色。這提供上下文，影響回應的語氣、深度與著眼點。比如「軟體架構師」和「初級開發者」或「安全審查員」給出的建議不同。

<img src="../../../translated_images/zh-MO/role-based-prompting.a806e1a73de6e3a4.webp" alt="基於角色的提示" width="800"/>

*設定上下文與身份——相同問題因指派角色不同而回應差異*

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

**使用時機：** 程式碼審查、教學、特定領域分析，或你需要回應符合特定專業層級與視角時。

### 提示範本

建立可重用的提示範本佔位符。無需每次撰寫新提示，只要定義一次範本並填入不同值即可。LangChain4j 透過 `PromptTemplate` 類與 `{{variable}}` 語法簡化此流程。

<img src="../../../translated_images/zh-MO/prompt-templates.14bfc37d45f1a933.webp" alt="提示範本" width="800"/>

*帶變數佔位符的可重用提示——一個範本，多種用途*

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

**使用時機：** 重複查詢不同輸入、批次處理、建立可重用 AI 工作流程，或任何提示結構固定但資料變動的情境。

---

這五項基礎為大多數提示任務提供穩固工具。接下來本模塊將在其基礎上加入 **八種進階模式**，利用 GPT-5.2 的推理控制、自我評估和結構化輸出能力。

## 進階模式

基礎講解完畢，接下來介紹使本模塊獨特的八種進階模式。並非所有問題都需同一策略。有些問題需要快速回應，有些需深入思考。部分需可見推理，另一些只需結果。以下每個模式針對不同場景優化——GPT-5.2 的推理控制更凸顯其差異。

<img src="../../../translated_images/zh-MO/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示工程模式" width="800"/>

*八種提示工程模式及其適用場合總覽*

<img src="../../../translated_images/zh-MO/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 的推理控制讓你指定模型思考多少——從快速直接回答到深度探索*

<img src="../../../translated_images/zh-MO/reasoning-effort.db4a3ba5b8e392c1.webp" alt="推理努力比較" width="800"/>

*低熱忱（快速直接）vs 高熱忱（全面探索）推理方法*

**低熱忱（快速且聚焦）** - 適合想要快速直接答案的簡單問題。模型做最少推理——最多 2 步驟。用於計算、查詢或簡單問題。

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

> 💡 **使用 GitHub Copilot 探索：** 打開 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 並詢問：
> - 「低熱忱和高熱忱提示模式有什麼差別？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「什麼時候應用自我反思模式，什麼時候用直接指令？」

**高熱忱（深度且全面）** - 貼合想要全面分析的複雜問題。模型充分探索並展示詳盡推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步推進）** - 面對多步驟工作流程。模型提供事先計劃，執行時持續說明，最後做總結。適用於遷移、實作或任何多段流程。

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

思維鏈提示明確要求模型展示推理過程，提升複雜任務準確度。逐步拆解幫助人和 AI 理解邏輯。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 詢問此模式：
> - 「如何讓任務執行模式適合長時間任務？」
> - 「在生產環境如何結構化工具前言的最佳實務？」
> - 「如何在 UI 中捕捉和顯示中間進度更新？」

<img src="../../../translated_images/zh-MO/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結，多步任務工作流程*

**自我反思程式碼** - 用於產出符合生產標準的程式碼。模型生成帶適當錯誤處理的高品質程式碼。用於開發新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-MO/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思週期" width="800"/>

*循環改進迴圈——生成、評估、識別問題、改進、反覆*

**結構化分析** - 用於一致評估。模型按照固定框架（正確性、實務、效能、安全、可維護性）審查程式碼。用於程式碼審查或品質評估。

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

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 詢問結構化分析：
> - 「如何為不同程式碼審查類型自訂分析框架？」
> - 「以程式化方式解析並處理結構化輸出的最佳方式？」
> - 「如何確保不同檢視階段的嚴重程度一致？」

<img src="../../../translated_images/zh-MO/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*建立一致審查框架及嚴重度分級*

**多輪對話** - 用於需要上下文的對話。模型記憶前文消息並累積上下文。適用於互動協助或複雜問答。

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

*對話上下文於多輪間累積直到達 token 限額*

**逐步推理** - 用於需明確邏輯的問題。模型顯示每步驟的明確推理。用於數學問題、邏輯題或你需要理解思考過程時。

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

*將問題拆解成明確的邏輯步驟*

**受限輸出** - 針對帶有特定格式要求的回應。模型嚴格遵守格式與長度規範。用於摘要或需要精確輸出結構時。

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

*強制特定格式、長度及結構要求*

## 使用現有 Azure 資源

**驗證部署：**

確認根目錄有 `.env` 檔案並含 Azure 認證（模塊 01 建立時）：
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用：**

> **注意：** 若你已使用模塊 01 的 `./start-all.sh` 啟動全部應用，本模塊已在 8083 埠執行。你可以跳過下面的啟動指令，直接訪問 http://localhost:8083。

**方案一：使用 Spring Boot 儀表板（推薦 VS Code 使用者）**

開發容器包含 Spring Boot 儀表板擴充，可視化管理所有 Spring Boot 應用。可在 VS Code 左側活動欄找到（尋找 Spring Boot 圖示）。
從 Spring Boot 儀表板，您可以：
- 查看工作區內所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 實時查看應用程式日誌
- 監控應用程式狀態

只需點擊「prompt-engineering」旁邊的播放按鈕，即可啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有網絡應用程式（模組 01-04）：

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

這兩個腳本會自動從根目錄的 `.env` 文件加載環境變數，且若 JAR 文件不存在，則會編譯生成。

> **注意：** 如果您希望在啟動前手動編譯所有模組：
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
./stop.sh  # 只有該模組
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

<img src="../../../translated_images/zh-MO/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主儀表板顯示所有 8 種提示工程模式及其特性和使用案例*

## 探索模式

網頁介面讓您嘗試不同的提示策略。每種模式解決不同問題——試試看，看看哪種方法在什麼情境下最突出。

### 低 vs 高 求知慾

使用低求知慾提出一個簡單問題，例如「200 的 15% 是多少？」您會得到即時且直接的答案。現在使用高求知慾問一個複雜問題：「設計一個高流量 API 的快取策略」。看模型如何放慢速度並提供詳細的推理。同一模型、同一問題結構——提示指示它需要做多少思考。

<img src="../../../translated_images/zh-MO/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*利用最少推理的快速計算*

<img src="../../../translated_images/zh-MO/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*完整的快取策略說明 (2.8MB)*

### 任務執行（工具開場白）

多步工作流程受益於事先規劃和過程描述。模型會先概述將做的事，敘述每一步，然後總結結果。

<img src="../../../translated_images/zh-MO/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*逐步敘述創建 REST 端點 (3.9MB)*

### 自我反思的代碼

試試「建立電子郵件驗證服務」。模型不僅生成代碼並停止，而是生成後評估品質標準，識別弱點並改進。您會看到它反覆迭代直到代碼達到生產標準。

<img src="../../../translated_images/zh-MO/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*完整電子郵件驗證服務 (5.2MB)*

### 結構化分析

代碼審查需要一致的評估框架。模型使用固定的分類（正確性、實踐、效能、安全性）以及嚴重等級來分析代碼。

<img src="../../../translated_images/zh-MO/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*基於框架的代碼審查*

### 多輪對話

問「什麼是 Spring Boot？」然後緊接著問「示範一個例子」。模型記住您第一個問題，並給出專門的 Spring Boot 範例。沒有記憶，第二個問題會過於模糊。

<img src="../../../translated_images/zh-MO/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*跨問題保持上下文*

### 逐步推理

挑一個數學問題，分別用逐步推理和低求知慾嘗試。低求知慾只給答案——快速但不透明。逐步推理會展示每項計算和決策。

<img src="../../../translated_images/zh-MO/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*帶明確步驟的數學問題*

### 約束輸出

當您需要特定格式或字數限制時，此模式強制嚴格遵守。試著用有序列點格式生成正好 100 字的摘要。

<img src="../../../translated_images/zh-MO/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*具有格式控制的機器學習摘要*

## 您真正學會的是

**推理努力決定一切**

GPT-5.2 讓您透過提示控制計算努力。低努力意味著快速回應和最少探索。高努力意味著模型會花時間深度思考。您學著根據任務複雜度調整努力——不要浪費時間在簡單問題，也不要匆忙做複雜決策。

**結構引導行為**

注意提示裡的 XML 標籤？它們不只是裝飾。模型比起自由格式文本，更能可靠地遵循結構化指令。當您需要多步驟流程或複雜邏輯時，結構幫助模型追蹤所在位置和下一步。

<img src="../../../translated_images/zh-MO/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*良好結構化提示的構成，包含清晰分區及 XML 式組織*

**自我評估提升品質**

自我反思模式透過明確品質標準運作。您不是希望模型「做對」，而是直接告訴它「對」是什麼：正確邏輯、錯誤處理、效能、安全性。模型能評估自身輸出並改進。這使代碼生成從抽獎變為流程。

**上下文有限**

多輪對話透過每次請求包含訊息歷史來運作。但有上限——每個模型都有最大 token 數。對話變長時，您需要策略保留相關上下文而不超限。本模組展示記憶如何工作；後續您將學會何時總結、何時忘記、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導航：** [← 上一章：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一章：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件是使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議採用專業人員的人工翻譯。我們不對因使用此翻譯而引起的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->