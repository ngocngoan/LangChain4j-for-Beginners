# Module 02: 使用 GPT-5.2 的提示工程

## 目錄

- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [了解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零樣本提示](../../../02-prompt-engineering)
  - [少樣本提示](../../../02-prompt-engineering)
  - [思路鏈](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有的 Azure 資源](../../../02-prompt-engineering)
- [應用截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低 vs 高熱情](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學到的是什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你將學到什麼

<img src="../../../translated_images/zh-MO/what-youll-learn.c68269ac048503b2.webp" alt="你將學到什麼" width="800"/>

在上一模組中，你了解了記憶如何使會話 AI 實現，並使用 GitHub 模型進行基本互動。現在我們將專注於如何提問——也就是使用 Azure OpenAI 的 GPT-5.2 所提供的提示本身。你如何結構提示，將極大影響你獲得回應的質量。我們將從基礎提示技術回顧開始，然後進入八個進階模式，全面利用 GPT-5.2 的能力。

我們採用 GPT-5.2 是因為它引入了推理控制——你可以告訴模型回答前應該思考多少。這使得不同提示策略更加明顯，並幫助你理解何時應用各種方法。我們也將受益於 Azure 對 GPT-5.2 較 GitHub 模型更寬鬆的速率限制。

## 先決條件

- 完成模組 01（已部署 Azure OpenAI 資源）
- 根目錄下有 `.env` 檔案，內含 Azure 認證（由模組 01 中的 `azd up` 建立）

> **注意：** 如果未完成模組 01，請先依照那裡的部署指示操作。

## 了解提示工程

<img src="../../../translated_images/zh-MO/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是關於設計輸入文本，讓你穩定地獲得所需結果。它不僅僅是提問，更是結構化請求，使模型完全理解你想要什麼及如何交付。

把它想像成給同事下指示。「修復錯誤」太模糊。「在 UserService.java 第 45 行添加 null 檢查以修復空指針異常」則很具體。語言模型同理——具體與結構很重要。

<img src="../../../translated_images/zh-MO/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的定位" width="800"/>

LangChain4j 提供架構——模型連接、記憶和訊息類型——而提示模式是你透過該架構傳送的精心結構文字。核心構建塊是 `SystemMessage`（設定 AI 行為和角色）和 `UserMessage`（承載你的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-MO/five-patterns-overview.160f35045ffd2a94.webp" alt="五個提示工程模式概覽" width="800"/>

在深入本模組的進階模式之前，讓我們回顧五種基礎提示技巧。這是每個提示工程師必須了解的構建基礎。如果你已經完成了[快速入門模組](../00-quick-start/README.md#2-prompt-patterns)，你已見過這些技巧的實作——以下是它們背後的概念架構。

### 零樣本提示

最簡單的方法：直接給模型指令，不帶示例。模型完全依賴訓練內容來理解並執行任務。這適用於預期行為明顯的簡單請求。

<img src="../../../translated_images/zh-MO/zero-shot-prompting.7abc24228be84e6c.webp" alt="零樣本提示" width="800"/>

*直接指示且無示例——模型單靠指令推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應：「正面」
```

**適用時機：** 簡單分類、直接問答、翻譯，或任何模型可不需額外指引即可處理的任務。

### 少樣本提示

提供範例展示你希望模型遵循的模式。模型從你的範例中學習輸入輸出格式，並將其套用到新輸入上。這大幅提升了欲求格式或行為不明顯任務的一致性。

<img src="../../../translated_images/zh-MO/few-shot-prompting.9d9eace1da88989a.webp" alt="少樣本提示" width="800"/>

*從範例學習——模型辨識模式並套用到新輸入*

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

**適用時機：** 自訂分類、一致格式、特定領域任務，或零樣本結果不一致時。

### 思路鏈

請模型逐步展示推理。模型不直接給答，而是分解問題並逐步展開。這提高了數學、邏輯、多步推理任務的準確性。

<img src="../../../translated_images/zh-MO/chain-of-thought.5cff6630e2657e2a.webp" alt="思路鏈提示" width="800"/>

*逐步推理——將複雜問題分解為明確邏輯步驟*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型顯示：15 - 8 = 7，然後 7 + 12 = 19 個蘋果
```

**適用時機：** 數學題、邏輯謎題、偵錯，或任何展現推理流程能提升準確與信任的任務。

### 基於角色的提示

在提問前設定 AI 的身份或角色。這提供上下文，影響回應的語氣、深度與焦點。軟體架構師給出的建議與初級開發者或安全審核員不同。

<img src="../../../translated_images/zh-MO/role-based-prompting.a806e1a73de6e3a4.webp" alt="基於角色的提示" width="800"/>

*設置上下文與身份——同一問題，根據不同角色有不同回應*

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

**適用時機：** 程式碼審查、教學、特定領域分析，或需要根據專業程度或視角定制回應時。

### 提示模板

建立可重用的提示，含可變佔位符。不用每次寫新提示，定義一次模板，再填入不同數值。LangChain4j 的 `PromptTemplate` 類用 `{{variable}}` 語法輕鬆實現。

<img src="../../../translated_images/zh-MO/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*含變數佔位符的可重用提示——一個模板，多種用法*

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

**適用時機：** 重複查詢不同輸入、批處理、構建可重用 AI 流程，或任何提示結構固定但資料不同的情況。

---

這五個基礎為你提供大部分提示任務的堅實工具組。本模組其餘內容基於它們，介紹**八種利用 GPT-5.2 推理控制、自我評估與結構化輸出能力的進階模式**。

## 進階模式

基礎夠了，接著來看看本模組獨有的八種進階模式。並非每個問題都用同一方法。有些問題需快速答覆，有些需深入思考。有些需顯示推理過程，有些僅需結果。以下每種模式針對不同場景優化——GPT-5.2 的推理控制使這些差異更明顯。

<img src="../../../translated_images/zh-MO/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示工程模式" width="800"/>

*八種提示工程模式及適用情境概覽*

<img src="../../../translated_images/zh-MO/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 推理控制允許你指定模型應思考多少──從快速直接回答到深入探索*

**低熱情（快速且專注）** - 適用簡單問題，追求快速、直接回答。模型進行最少推理，最多兩步。用於計算、查詢、或直接問題。

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
> - 「低熱情和高熱情提示模式有什麼不同？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回答？」
> - 「何時使用自我反思模式，何時用直接指令？」

**高熱情（深入且全面）** - 複雜問題需全面分析時用。模型徹底探索並展示詳細推理。適用系統設計、架構決策、複雜研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步推進）** - 用於多步驟工作流程。模型先提出計劃，執行時逐步敘述，最後總結。適用遷移、實作或任何多步過程。

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

思路鏈提示明確要求模型展示推理過程，提高複雜任務準確率。逐步拆解使人與 AI 都能理解邏輯。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 詢問此模式：
> - 「如何調整任務執行模式於長時間運行的操作？」
> - 「生產環境中結構化工具前言的最佳實踐是什麼？」
> - 「如何在 UI 捕獲並顯示中間進度？」

<img src="../../../translated_images/zh-MO/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結的多步任務工作流程*

**自我反思代碼** - 生成符合生產標準且具錯誤處理的程式碼。用於新功能或服務構建。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-MO/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改進循環 - 生成、評估、發現問題、改進、重複*

**結構化分析** - 用固定框架進行一致評估。模型依正確性、慣例、效能、安全與維護性進行程式碼審查。適用於程式碼評審或品質評估。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試結構化分析：**
> - 「如何針對不同類型程式碼審查自訂分析框架？」
> - 「如何以程式方式解析並行動結構化輸出？」
> - 「怎麼確保不同審查會議間一致的嚴重性等級？」

<img src="../../../translated_images/zh-MO/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*用嚴重性等級實現一致程式碼審查的框架*

**多輪對話** - 需上下文維持的對話。模型記住先前訊息並以此為基礎。用於互動幫助或複雜問答。

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

*多輪對話中如何累積上下文，直到達到 token 限制*

**逐步推理** - 需顯示邏輯的問題。模型逐步展示每一步推理。用於數學題、邏輯謎題或需要理解思考過程時。

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

**受限輸出** - 對格式有特定要求的回應。模型嚴格遵守格式與長度規則。適用於摘要或需精確輸出結構的場合。

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

*強制特定格式、長度與結構規格*

## 使用現有的 Azure 資源

**驗證部署：**

確保根目錄存在 `.env` 檔案，內含 Azure 認證（模組 01 建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用：**

> **注意：** 若已使用模組 01 的 `./start-all.sh` 啟動所有應用，本模組已在 8083 埠運行。可跳過下列啟動指令，直接前往 http://localhost:8083。

**選項一：使用 Spring Boot Dashboard（建議 VS Code 用戶）**

開發容器內含 Spring Boot Dashboard 擴充，提供視覺介面管理所有 Spring Boot 應用。你可以在 VS Code 左側活動列找到它（尋找 Spring Boot 圖示）。

透過 Spring Boot Dashboard，你可以：
- 查看工作區內所有可用的 Spring Boot 應用
- 一鍵啟動/停止應用
- 實時查看應用日誌
- 監控應用狀態
只需點擊「prompt-engineering」旁的播放按鈕開始此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot 儀表板" width="400"/>

**選項二：使用 shell 腳本**

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

這些腳本會自動從根目錄 `.env` 檔載入環境變數，若 JAR 檔不存在也會自行構建。

> **注意：** 如果您想先手動構建所有模組再啟動：
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

在您的瀏覽器中開啟 http://localhost:8083。

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
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 應用程式截圖

<img src="../../../translated_images/zh-MO/dashboard-home.5444dbda4bc1f79d.webp" alt="儀表板首頁" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主要儀表板顯示全部 8 種 prompt 工程範例及其特性和使用情境*

## 探索這些範例

網頁介面讓您嘗試不同的提示策略。每種範例解決不同的問題——試試看哪種方法適用於何時。

### 低急切度 與 高急切度

用低急切度問一個簡單問題，例如「15% 的 200 是多少？」會立即得到直接答案。現在用高急切度問一個複雜問題，例如「設計高流量 API 的快取策略」。注意模型如何放慢速度並給出詳細推理。相同模型，相同問題架構——提示決定它要思考多少。

<img src="../../../translated_images/zh-MO/low-eagerness-demo.898894591fb23aa0.webp" alt="低急切度示範" width="800"/>

*快速計算，幾乎無推理*

<img src="../../../translated_images/zh-MO/high-eagerness-demo.4ac93e7786c5a376.webp" alt="高急切度示範" width="800"/>

*全面的快取策略（2.8MB）*

### 任務執行（工具前言）

多步驟工作流程需事先規劃與進度說明。模型會概述將做什麼、逐步解說，最後總結結果。

<img src="../../../translated_images/zh-MO/tool-preambles-demo.3ca4881e417f2e28.webp" alt="任務執行示範" width="800"/>

*逐步說明建立 REST 端點（3.9MB）*

### 自我反思的程式碼

試試「建立 email 驗證服務」。模型不只生成程式碼後停下，而是生成、針對品質標準評估、找出弱點並改進。您會看到它反覆迭代直到達到生產標準。

<img src="../../../translated_images/zh-MO/self-reflecting-code-demo.851ee05c988e743f.webp" alt="自我反思程式碼示範" width="800"/>

*完整的 email 驗證服務（5.2MB）*

### 結構化分析

程式碼審查需要一致的評估框架。模型使用固定類別（正確性、慣例、效能、安全性）及嚴重度等級來分析程式碼。

<img src="../../../translated_images/zh-MO/structured-analysis-demo.9ef892194cd23bc8.webp" alt="結構化分析示範" width="800"/>

*基於框架的程式碼審查*

### 多回合對話

問「什麼是 Spring Boot？」接著立刻追問「給我一個範例」。模型會記住您第一次問題，專門給您一個 Spring Boot 範例。沒有記憶，第二個問題太模糊無法回答。

<img src="../../../translated_images/zh-MO/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="多回合對話示範" width="800"/>

*跨問題的上下文保存*

### 逐步推理

選一道數學題，用逐步推理和低急切度各試一次。低急切度只給答案——快速但不透明。逐步推理展示每個計算和決策。

<img src="../../../translated_images/zh-MO/step-by-step-reasoning-demo.12139513356faecd.webp" alt="逐步推理示範" width="800"/>

*附明確步驟的數學題解*

### 限制型輸出

當您需要特定格式或字數時，此範例強制嚴格遵守。試著生成剛好 100 字且用項目符號格式的摘要。

<img src="../../../translated_images/zh-MO/constrained-output-demo.567cc45b75da1633.webp" alt="限制型輸出示範" width="800"/>

*機器學習摘要及格式控制*

## 您真正學到的是

**推理努力改變一切**

GPT-5.2 讓您透過提示控制計算努力度。低努力代表快速答覆並少量探索。高努力代表模型花時間深入思考。您正在學會依任務複雜度匹配努力——勿將時間浪費在簡單問題，也別急促複雜決策。

**結構引導行為**

注意提示中的 XML 標籤？這不是裝飾。模型比起自由格式文字，更可靠跟從結構化指令。當您需要多步驟流程或複雜邏輯，結構能幫助模型追蹤目前位置以及下一步。

<img src="../../../translated_images/zh-MO/prompt-structure.a77763d63f4e2f89.webp" alt="提示結構" width="800"/>

*良好結構化提示的構造，清晰分段與 XML 樣式組織*

**透過自我評估達成高品質**

自我反思範例將品質標準明確化。您不用僅祈願模型「做對」，而是告訴它什麼叫「對」：邏輯正確、錯誤處理、效能、安全性。模型因此能自我評估並改進。讓程式碼生成不再是賭博，而是一種流程。

**上下文有限**

多回合對話是每次請求都包含訊息紀錄。但有上限——每個模型的最大 token 數。隨著對話增加，您需要策略保持相關上下文同時避免超限。本模組示範記憶如何運作；後續您將學會何時摘要、何時遺忘、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導覽：** [← 上一頁：模組 01 - 介紹](../01-introduction/README.md) | [回主頁](../README.md) | [下一頁：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我們致力於確保翻譯的準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。文件的原始語言版本應視為權威版本。對於重要資訊，建議採用專業人工翻譯。我們對因使用此翻譯而引起的任何誤解或曲解概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->