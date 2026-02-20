# Module 02: 使用 GPT-5.2 的提示工程

## 目錄

- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [了解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少量示例提示](../../../02-prompt-engineering)
  - [思維鏈提示](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有的 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索這些模式](../../../02-prompt-engineering)
  - [低積極 vs 高積極](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思程式碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的內容](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你將學到什麼

<img src="../../../translated_images/zh-TW/what-youll-learn.c68269ac048503b2.webp" alt="你將學到什麼" width="800"/>

在上一個模組中，你了解了記憶如何促進對話式 AI，並使用 GitHub Models 進行基本互動。現在我們將專注於你如何提問——提示本身——使用 Azure OpenAI 的 GPT-5.2。你如何結構提示，會大大影響你獲得回答的質量。我們先回顧基本提示技術，再進入八種進階模式，充分利用 GPT-5.2 的能力。

我們使用 GPT-5.2 是因為它引入了推理控制——你可以告訴模型在回答前要思考多少。這讓不同提示策略更加明顯，也有助於你了解何時使用哪種方法。相比 GitHub Models，我們還能享受 Azure 對 GPT-5.2 較低的速率限制。

## 先決條件

- 完成模組 01（已部署 Azure OpenAI 資源）
- 根目錄中有 `.env` 檔案包含 Azure 認證（由模組 01 中 `azd up` 建立）

> **注意：** 如果你還沒完成模組 01，請先跟隨那邊的部署指南。

## 了解提示工程

<img src="../../../translated_images/zh-TW/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文本，使你能穩定取得想要結果。它不只是問問題——而是結構化請求，讓模型完全理解你想要什麼，如何給出答案。

可以想像像是在給同事下指令。「修 bug」很模糊，「在 UserService.java 第 45 行透過加上 null 檢查修正 null pointer 例外」則很具體。語言模型也是如此——具體和結構很重要。

<img src="../../../translated_images/zh-TW/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的定位" width="800"/>

LangChain4j 提供基礎架構——模型連接、記憶與訊息類型——而提示模式只是你透過這些基礎架構傳遞的精心結構化文本。關鍵組件是 `SystemMessage`（設定 AI 的行為與角色）跟 `UserMessage`（攜帶你的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-TW/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式概述" width="800"/>

在進入本模組進階模式前，先回顧五種基礎提示技巧。這是每個提示工程師都應該知道的基本組件。如果你曾經使用過[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，你應該已見過這些實作——這是背後的概念框架。

### 零次提示

最簡單的方法：給模型直接指示，無須範例。模型完全依賴訓練來理解和執行任務。這適合預期行為明確的直接請求。

<img src="../../../translated_images/zh-TW/zero-shot-prompting.7abc24228be84e6c.webp" alt="零次提示" width="800"/>

*無範例的直接指示——模型從指令中推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應："正面"
```

**使用時機：** 簡單的分類、直接問題、翻譯，或模型能無需額外指導處理的任務。

### 少量示例提示

提供範例來示範你希望模型遵循的模式。模型從你給的範例學習預期的輸入輸出格式，並應用於新輸入。這能大幅提升欲求格式或行為不明顯任務的一致性。

<img src="../../../translated_images/zh-TW/few-shot-prompting.9d9eace1da88989a.webp" alt="少量示例提示" width="800"/>

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

**使用時機：** 自訂分類、一致格式、領域特定任務，或零次提示結果不穩定時。

### 思維鏈提示

請模型展示逐步推理。模型不會直接跳到答案，而是將問題拆解，逐步處理每部分。這提升數學、邏輯、多步推理等任務的準確度。

<img src="../../../translated_images/zh-TW/chain-of-thought.5cff6630e2657e2a.webp" alt="思維鏈提示" width="800"/>

*逐步推理——將複雜問題拆解成明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**使用時機：** 數學問題、邏輯謎題、除錯，或任何透過展示推理過程提升準確度與信任感的任務。

### 基於角色的提示

在提問前設定 AI 角色或身分。此上下文會調整回答的語氣、深度與重點。比方說「軟體架構師」會提供不同於「初級開發者」或「資安稽核員」的建議。

<img src="../../../translated_images/zh-TW/role-based-prompting.a806e1a73de6e3a4.webp" alt="基於角色的提示" width="800"/>

*設定上下文與身分——同樣問題依角色分別得到不同回答*

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

**使用時機：** 程式碼審查、輔導、領域分析，或需要針對特定專業層級或觀點定製回答時。

### 提示模板

建立可重複使用且含變數佔位符的提示。不是每次都寫新提示，而是定義一個模板，根據情況填入不同值。LangChain4j 的 `PromptTemplate` 類利用 `{{variable}}` 語法方便實現。

<img src="../../../translated_images/zh-TW/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*可重用且帶變數佔位符的提示——一個模板，多種用途*

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

**使用時機：** 重複查詢但輸入不同、批次處理、構建可重用 AI 流程，或任何提示結構固定、資料改變的場景。

---

這五個基礎技巧讓你擁有大多數提示任務的堅實工具組。本模組的其餘內容基於此，展開八種利用 GPT-5.2 的推理控制、自我評價與結構化輸出能力的**進階模式**。

## 進階模式

基礎說明完畢，現在來看使本模組獨特的八種進階模式。不是所有問題都適合用同一方法。有些問題要快速回答，有些需深入思考。有些需要可見推理，有些只要結果。以下每種模式都針對不同場景優化——而 GPT-5.2 的推理控制讓差異更明顯。

<img src="../../../translated_images/zh-TW/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示模式" width="800"/>

*八種提示工程模式與其使用場合總覽*

<img src="../../../translated_images/zh-TW/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 的推理控制讓你指定模型思考程度——從快速直接答案到深入探索*

**低積極（快速且聚焦）** - 適用於你想要快速直接回答的簡單問題。模型推理步驟少，最多 2 步。用在計算、查詢或直接問題。

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

> 💡 **用 GitHub Copilot 探索：**開啟 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 並問：
> - 「低積極和高積極提示模式差異是什麼？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回答？」
> - 「什麼時候該用自我反思模式，什麼時候該直接指令？」

**高積極（深入且全面）** - 適用於你想要全面分析的複雜問題。模型徹底挖掘並展示詳細推理。適合系統設計、架構決策或複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進度）** - 用於多步驟工作流程。模型提供前置計畫，逐步敘述過程後給出總結。適用遷移、實作或任何多步流程。

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

思維鏈提示明確要求模型展示推理過程，提升複雜任務的準確度。逐步拆解幫助人類與 AI 理解邏輯。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：**問這個模式：
> - 「如何為長時間運作任務調整任務執行模式？」
> - 「在生產應用中如何結構化工具前言較佳？」
> - 「如何擷取並在 UI 顯示中間進度？」

<img src="../../../translated_images/zh-TW/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計畫 → 執行 → 總結的多步任務流程*

**自我反思程式碼** - 生成符合生產標準的高品質程式碼。模型產生具備完善錯誤處理的程式碼。用於開發新功能或服務。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-TW/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改善迴圈——生成、評估、識別問題、改進，重複*

**結構化分析** - 用於一致性評估。模型依固定框架（正確性、慣例、效能、安全、可維護性）審查程式碼。適用程式碼審查或品質評估。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：**問結構化分析：
> - 「如何針對不同程式碼審查自訂分析框架？」
> - 「以程式方式解析與處理結構化輸出最佳方案？」
> - 「如何確保不同審查會議中的嚴重程度一致？」

<img src="../../../translated_images/zh-TW/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*促進具嚴重度分級的一致程式碼審查框架*

**多輪對話** - 適用於需要上下文的對話。模型記憶先前訊息並沿用。用於互動幫助或複雜問答。

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

*對話上下文隨多輪累積直到達令牌限制*

**逐步推理** - 用於需要邏輯可視化的問題。模型展示每步明確推理。用在數學問題、邏輯謎題，或你想理解思考過程時。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-TW/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="逐步模式" width="800"/>

*將問題拆成明確邏輯步驟*

**受限輸出** - 用於回答需特定格式要求。模型嚴格遵從格式與長度規則。適合摘要或需要精確輸出的情況。

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

*強制特定格式、長度與結構的要求*

## 使用現有的 Azure 資源

**確認部署：**

確保根目錄存在 `.env` 檔案，內含 Azure 認證（模組 01 部署時建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已透過模組 01 的 `./start-all.sh` 啟動所有服務，本模組已在 8083 端口運行。可跳過下列啟動步驟，直接訪問 http://localhost:8083 。

**選項 1：使用 Spring Boot 儀表板（建議 VS Code 使用者）**

開發容器內含 Spring Boot 儀表板擴充功能，提供管理所有 Spring Boot 應用的視覺介面。你可以在 VS Code 左側活動欄中找到（尋找 Spring Boot 圖示）。

從 Spring Boot 儀表板，你可以：
- 查看工作區所有可用 Spring Boot 應用
- 一鍵啟動或停止應用
- 實時查看應用程式日誌
- 監控應用狀態
只要點擊「prompt-engineering」旁邊的播放按鈕即可啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項二：使用 shell 腳本**

啟動所有網頁應用程式（模組 01-04）：

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

兩個腳本都會自動從專案根目錄的 `.env` 檔案載入環境變數，並在 JAR 檔案不存在時自動編譯。

> **注意：** 如果你想要手動先編譯所有模組，再啟動的話：
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

請在瀏覽器中打開 http://localhost:8083 。

**停止方式：**

**Bash:**
```bash
./stop.sh  # 僅此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式畫面截圖

<img src="../../../translated_images/zh-TW/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主控台顯示所有 8 種提示工程模式及其特性與使用案例*

## 探索提示模式

網頁介面讓你可以嘗試不同的提示策略。每種模式解決不同的問題——試試看它們各自何時最適用。

### 低積極度 vs 高積極度

使用低積極度問一個簡單問題，例如「200 的 15% 是多少？」你會立即得到直接答案。現在用高積極度問一個複雜問題，如「為高流量 API 設計緩存策略」。你會看到模型放慢速度並給出詳細推理。模型與問題結構相同——但提示告訴它要進行多少思考。

<img src="../../../translated_images/zh-TW/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*迅速計算，幾乎不需推理*

<img src="../../../translated_images/zh-TW/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*全面的緩存策略說明 (2.8MB)*

### 任務執行（工具前言）

多步驟工作流程受益於事先規劃和過程描述。模型會列出其執行步驟，逐步講解，最後總結結果。

<img src="../../../translated_images/zh-TW/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*以逐步講解方式建立 REST 端點（3.9MB）*

### 自我反思程式碼

嘗試「建立電子郵件驗證服務」。模型不只是產生程式碼後停止，而是生成、根據品質標準評估、找出弱點並改進。你會看到它不斷迭代直到程式碼達到生產標準。

<img src="../../../translated_images/zh-TW/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*完整的電子郵件驗證服務 (5.2MB)*

### 結構化分析

程式碼審查需要統一評估架構。模型使用固定分類（正確性、實務、效能、安全性）並搭配嚴重等級來分析程式碼。

<img src="../../../translated_images/zh-TW/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*基於框架的程式碼審查*

### 多輪對話

先問「什麼是 Spring Boot？」然後緊接著問「給我一個範例」。模型會記住你的第一個問題，專門給你一個 Spring Boot 範例。若無記憶能力，第二個問題會太模糊。

<img src="../../../translated_images/zh-TW/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*跨問題的上下文保存*

### 逐步推理

挑一個數學問題，分別用逐步推理與低積極度嘗試。低積極度只給答案——快速但不透明。逐步推理會顯示每個計算與決策過程。

<img src="../../../translated_images/zh-TW/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*明確步驟的數學問題解答*

### 限制輸出

當你需要特定格式或字數時，這個模式可嚴格遵守規範。試試用 100 個字且列點格式產生摘要。

<img src="../../../translated_images/zh-TW/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*機器學習摘要，具格式控制*

## 你真正學到的是什麼

**推理力度改變一切**

GPT-5.2 讓你透過提示控制計算力度。低力度代表快速回應且少探究。高力度代表模型花時間深入思考。你正在學習匹配力度與任務複雜度——簡單問題不要浪費時間，複雜決策也別倉促。

**結構引導行為**

注意提示中的 XML 標籤？它們不是裝飾。模型比起自由文字，更可靠地遵守結構化指令。當你需要多步驟流程或複雜邏輯時，結構可協助模型追蹤當前與下一步。

<img src="../../../translated_images/zh-TW/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*結構完整、段落清楚且以 XML 風格組織的提示組成*

**透過自我評估提升品質**

自我反思模式會明確定義品質標準。不只是希望模型「做對」，而是告訴它「對」的標準：正確邏輯、錯誤處理、效能、安全。模型就能評估自己的輸出並加以改進。讓程式碼生成成為可控的流程，而非運氣。

**上下文是有限資源**

多輪對話運作靠每次請求帶入訊息歷史。但有上限——每個模型都有最大 token 數。隨對話增加，你需要策略維持相關上下文且不超標。本模組會示範記憶運作；未來你將學會何時摘要、何時遺忘、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG (檢索增強生成)](../03-rag/README.md)

---

**導覽：** [← 上一頁：模組 01 - 介紹](../01-introduction/README.md) | [回主選單](../README.md) | [下一頁：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用此翻譯而產生的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->