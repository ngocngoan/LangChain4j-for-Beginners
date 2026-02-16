# Module 02: 使用 GPT-5.2 進行提示工程

## 目錄

- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基礎](../../../02-prompt-engineering)
  - [零樣本提示](../../../02-prompt-engineering)
  - [少樣本提示](../../../02-prompt-engineering)
  - [思路鏈](../../../02-prompt-engineering)
  - [基於角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [進階模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索提示模式](../../../02-prompt-engineering)
  - [低熱忱與高熱忱](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思程式碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [限制輸出](../../../02-prompt-engineering)
- [你真正在學什麼](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你將學到什麼

<img src="../../../translated_images/zh-HK/what-youll-learn.c68269ac048503b2.webp" alt="你將學到什麼" width="800"/>

在上一個模塊中，你已見識到記憶如何促進對話型 AI，並使用 GitHub 模型進行基本互動。現在我們將聚焦於如何提問——即提示本身——使用 Azure OpenAI 的 GPT-5.2。你如何結構提示會大幅影響你獲得的回應質量。我們先回顧基本的提示技術，然後進入八種進階模式，充分利用 GPT-5.2 的強大功能。

我們會使用 GPT-5.2，因為它引入了推理控制——你可以告訴模型在回答前要思考多少。這讓不同的提示策略更加明顯，並幫助你理解何時使用哪種方法。另外，相較於 GitHub 模型，Azure 對 GPT-5.2 設有更少的速率限制。

## 先決條件

- 完成模塊 01（已部署 Azure OpenAI 資源）
- 根目錄中存在 `.env` 檔案，內含 Azure 賬戶憑證（由模塊 01 的 `azd up` 創建）

> **注意：** 如果你還未完成模塊 01，請先按照那裡的部署指示操作。

## 理解提示工程

<img src="../../../translated_images/zh-HK/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什麼是提示工程？" width="800"/>

提示工程是設計輸入文本，使你穩定得到所需結果的技術。它不僅僅是提問，而是要結構化請求，讓模型精確明白你想要什麼以及如何交付。

把它想像成給同事下指令。「修復錯誤」太模糊。「在 UserService.java 第 45 行加入 null 檢查以修正 null pointer 例外」則非常具體。語言模型也是如此——明確性與結構都很重要。

<img src="../../../translated_images/zh-HK/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 如何匹配" width="800"/>

LangChain4j 提供了基礎設施——模型連接、記憶和消息類型——而提示模式只是經過精心結構化的文本，透過這些基礎設施發送。核心組成是 `SystemMessage`（設定 AI 的行為和角色）和 `UserMessage`（攜帶你的實際請求）。

## 提示工程基礎

<img src="../../../translated_images/zh-HK/five-patterns-overview.160f35045ffd2a94.webp" alt="五種提示工程模式概述" width="800"/>

在深入本模塊的進階模式前，讓我們回顧五種基礎提示技巧。這些是每個提示工程師必知的基石。如果你已完成[快速入門模塊](../00-quick-start/README.md#2-prompt-patterns)，你已見過它們的實作——這裡展示的是背後的概念架構。

### 零樣本提示

最簡單的方法：給模型一個直接指示，沒有範例。模型完全依賴其訓練來理解並執行任務。這對於預期行為明顯的直接請求很有效。

<img src="../../../translated_images/zh-HK/zero-shot-prompting.7abc24228be84e6c.webp" alt="零樣本提示" width="800"/>

*無範例的直接指令——模型僅從指示推斷任務*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回應：「正面」
```

**何時使用：** 簡單分類、直接問答、翻譯，或任何模型能在無額外引導下處理的任務。

### 少樣本提示

提供範例示範你想讓模型遵循的模式。模型從你的範例學習期望的輸入─輸出格式，並將其應用於新輸入。這大幅提升在格式或行為不明顯任務上的一致性。

<img src="../../../translated_images/zh-HK/few-shot-prompting.9d9eace1da88989a.webp" alt="少樣本提示" width="800"/>

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

**何時使用：** 自訂分類、一致格式化、特定領域任務，或零樣本結果不穩定時。

### 思路鏈

讓模型逐步展示推理過程。模型不直接跳至答案，而是分解問題，明確處理每部分。這提升數學、邏輯和多步推理任務的精準度。

<img src="../../../translated_images/zh-HK/chain-of-thought.5cff6630e2657e2a.webp" alt="思路鏈提示" width="800"/>

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

**何時使用：** 數學問題、邏輯謎題、除錯，或任何展示推理過程能提升準確度和信任感的任務。

### 基於角色的提示

在提問前設定 AI 的身份或角色。這提供背景，影響回答的語氣、深度和焦點。「軟件架構師」的建議會與「初級開發者」或「安全審核員」不同。

<img src="../../../translated_images/zh-HK/role-based-prompting.a806e1a73de6e3a4.webp" alt="基於角色的提示" width="800"/>

*設定背景和角色——同一問題根據不同角色會得到不同回應*

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

**何時使用：** 程式碼審查、教學、領域特定分析，或需要根據專業水平或視角調整回答時。

### 提示模板

創建可重用的帶有變量佔位符的提示。不必每次撰寫新提示，先定義模板，再填入不同數值。LangChain4j 的 `PromptTemplate` 類使用 `{{variable}}` 語法很方便。

<img src="../../../translated_images/zh-HK/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*帶變量佔位符的可重用提示——一個模板，多種用途*

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

**何時使用：** 重複查詢不同輸入、批量處理、建立可重用 AI 工作流程，或提示結構不變但數據變化時。

---

這五大基礎為你提供大部分提示任務的紮實工具。接下來本模塊將基於它們，介紹**八種進階模式**，利用 GPT-5.2 的推理控制、自我評估及結構化輸出功能。

## 進階模式

基礎覆蓋後，我們來看看讓本模塊獨特的八種進階模式。不是所有問題都需用同樣方法。有的問題需要快速回應，有的則需深度思考。有的需看見推理過程，有的只要結果。以下每種模式為不同情境最佳化——而 GPT-5.2 的推理控制讓差異更加明顯。

<img src="../../../translated_images/zh-HK/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八種提示模式" width="800"/>

*八種提示工程模式及其使用場景概覽*

<img src="../../../translated_images/zh-HK/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 推理控制，讓你指定模型思考程度——從快速直接回答到深入探索*

<img src="../../../translated_images/zh-HK/reasoning-effort.db4a3ba5b8e392c1.webp" alt="推理努力程度比較" width="800"/>

*低熱忱（快速、直接） vs 高熱忱（透徹、探索）推理方法*

**低熱忱（快速且聚焦）** - 適用於想要快速、直接回覆的簡單問題。模型的推理步驟非常少——最多 2 步。適合計算、查詢或直接問題。

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **用 GitHub Copilot 探索：** 打開 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)，試問：
> - 「低熱忱和高熱忱提示模式有何不同？」
> - 「提示裡的 XML 標籤如何協助結構化 AI 回答？」
> - 「什麼時候該用自我反思模式 vs 直接指令？」

**高熱忱（深入且透徹）** - 適用於需要全面分析的複雜問題。模型會徹底探索並展示詳盡推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** - 用於多步工作流程。模型先提供計劃，執行時逐步講解，再給出總結。用於遷移、實作或任何多步流程。

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

思路鏈提示明確要求模型展示推理過程，提升複雜任務的準確度。逐步拆解有助人類與 AI 共同理解邏輯。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 詢問此模式：
> - 「如何針對長時間運行操作調整任務執行模式？」
> - 「生產應用中工具前言結構的最佳實踐是什麼？」
> - 「如何在 UI 中捕捉並顯示中間進度更新？」

<img src="../../../translated_images/zh-HK/task-execution-pattern.9da3967750ab5c1e.webp" alt="任務執行模式" width="800"/>

*計劃 → 執行 → 總結，適用於多步任務流程*

**自我反思程式碼** - 生產級代碼生成。模型產生代碼，根據質量標準檢查並迭代改進。用於開發新功能或服務。

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-HK/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循環" width="800"/>

*迭代改進循環——生成、評估、識別問題、優化、循環*

**結構化分析** - 用於穩定評估。模型依固定框架（正確性、實踐、效能、安全）審查代碼。適用於代碼審查或質量評估。

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試問結構化分析：**
> - 「如何為不同類型代碼審查定制分析框架？」
> - 「以程式方式解析和處理結構化輸出的最佳方法是什麼？」
> - 「如何確保不同審查中嚴重程度的一致性？」

<img src="../../../translated_images/zh-HK/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="結構化分析模式" width="800"/>

*四類框架搭配嚴重程度，用於一致的代碼審查*

**多輪對話** - 適合需要上下文記憶的對話。模型記住先前訊息並基於它們回覆。適合互動式幫助或複雜問答。

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

*多輪對話中，上下文如何累積直到達到 token 限制*

**逐步推理** - 適用於需要可見邏輯的問題。模型對每步驟明確展示推理。用於數學問題、邏輯謎題，或需理解思維過程時。

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

*將問題拆為明確邏輯步驟*

**限制輸出** - 回覆需符合特定格式要求。模型嚴格遵守格式和長度。用於摘要或需精確輸出結構時。

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

<img src="../../../translated_images/zh-HK/constrained-output-pattern.0ce39a682a6795c2.webp" alt="限制輸出模式" width="800"/>

*強制符合特定格式、長度及結構需求*

## 使用現有 Azure 資源

**驗證部署狀況：**

確保根目錄中有 `.env` 檔案，包含 Azure 賬戶憑證（模塊 01 部署時創建）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若你已在模塊 01 用 `./start-all.sh` 啟動所有應用，本模塊已在 8083 埠運行。可省略以下啟動指令，直接訪問 http://localhost:8083。

**選項 1：使用 Spring Boot 控制台（推薦 VS Code 用戶）**

開發容器包含 Spring Boot Dashboard 擴充，提供圖形介面管理所有 Spring Boot 應用。你可在 VS Code 左側活動欄中找到它（尋找 Spring Boot 圖示）。
從 Spring Boot Dashboard，您可以：
- 查看工作區內所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 即時查看應用程式日誌
- 監控應用程式狀態

只需點擊「prompt-engineering」旁的播放按鈕即可啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有 Web 應用程式（模組 01-04）：

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

或僅啟動此模組：

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

這些腳本會自動從根目錄的 `.env` 檔案載入環境變數，並在 JAR 檔不存在時自動建置。

> **注意：** 如果您偏好在啟動前手動建置所有模組：
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
./stop.sh  # 只有此模組
# 或者
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

*主控制面板顯示全部 8 個提示工程模式及其特性與使用案例*

## 探索各種模式

網頁介面讓您嘗試不同的提示策略。每種模式解決不同的問題——試試看，發掘每種方式的優勢所在。

### 低投入與高投入

用低投入問一個簡單問題，例如「200 的 15% 是多少？」您會立即得到直接答案。再用高投入問複雜問題，如「為高流量 API 設計快取策略」。您會看到模型放慢速度，並提供詳細推理。相同模型、相同問題結構，但提示決定模型思考深度。

<img src="../../../translated_images/zh-HK/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*快速計算，推理最少*

<img src="../../../translated_images/zh-HK/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*全面快取策略（2.8MB）*

### 任務執行（工具前置語句）

多步驟工作流程受益於預先規劃和進度敘述。模型會列出待做事項，敘述每一步，最後總結結果。

<img src="../../../translated_images/zh-HK/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*逐步敘述創建 REST 端點（3.9MB）*

### 自我反思程式碼

試試「建立一個電子郵件驗證服務」。模型不只是生成程式碼就停，而是生成後依品質標準評估，找出弱點並改進。您會看到它不斷迭代，直到程式碼符合生產標準。

<img src="../../../translated_images/zh-HK/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*完整電子郵件驗證服務（5.2MB）*

### 結構化分析

程式碼審查需要一致的評估框架。模型使用固定類別（正確性、實踐、效能、安全性）與嚴重程度進行分析。

<img src="../../../translated_images/zh-HK/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*基於框架的程式碼審查*

### 多輪對話

問「Spring Boot 是什麼？」接著馬上問「示範一個例子」。模型會記住您的第一個問題，並針對 Spring Boot 給您範例。若無記憶，第二個問題會太模糊。

<img src="../../../translated_images/zh-HK/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*問題間的上下文保存*

### 逐步推理

選個數學問題，分別用逐步推理和低投入測試。低投入只給答案——快速但不透明。逐步推理會展示每個計算與決策。

<img src="../../../translated_images/zh-HK/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*明確步驟的數學問題*

### 限制輸出

當您需要特定格式或字數時，此模式會嚴格遵守。試試用點列格式產生正好 100 字的摘要。

<img src="../../../translated_images/zh-HK/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*受控格式的機器學習摘要*

## 您真正學習的是什麼

**推理投入改變一切**

GPT-5.2 允許您通過提示控制計算投入。低投入代表快速回應、簡單探索。高投入則代表模型花時間深度思考。您學會匹配投入與任務複雜度——別浪費時間在簡單問題上，也別急於做複雜決策。

**結構引導行為**

注意提示裡的 XML 標籤？它們不是裝飾。相較自由文本，模型更可靠地遵循結構化指令。當您需要多步驟流程或複雜邏輯，結構幫助模型追蹤進度和下一步。

<img src="../../../translated_images/zh-HK/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*結構良好的提示組成，清楚分段及 XML 風格組織*

**品質來自自我評估**

自我反思模式的運作方式是明確指出品質標準。您不必指望模型“做對”，而是告訴它“正確”是什麼：正確邏輯、錯誤處理、效能、安全性。模型可以自我評估產出並改進。程式碼生成因此從抽獎變成可控流程。

**上下文有限**

多輪對話靠隨請求附上訊息歷史。但有限制——每個模型有最大代幣數。當對話增長，您需策略維持相關上下文而不超限。本模組教您記憶如何運作；稍後您會學到何時摘要、何時遺忘、何時檢索。

## 下一步

**下一模組：** [03-rag - RAG（結合檢索的生成）](../03-rag/README.md)

---

**導覽：** [← 上一節：模組 01 - 簡介](../01-introduction/README.md) | [返回主頁](../README.md) | [下一節：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。因使用本翻譯而產生的任何誤解或誤釋，我們概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->