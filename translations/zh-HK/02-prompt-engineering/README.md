# Module 02: 使用 GPT-5.2 的提示工程

## 目錄

- [你將學到什麼](../../../02-prompt-engineering)
- [先決條件](../../../02-prompt-engineering)
- [了解提示工程](../../../02-prompt-engineering)
- [如何使用 LangChain4j](../../../02-prompt-engineering)
- [核心模式](../../../02-prompt-engineering)
- [使用現有 Azure 資源](../../../02-prompt-engineering)
- [應用程式截圖](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低熱情 vs 高熱情](../../../02-prompt-engineering)
  - [任務執行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代碼](../../../02-prompt-engineering)
  - [結構化分析](../../../02-prompt-engineering)
  - [多輪對話](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限輸出](../../../02-prompt-engineering)
- [你真正學習的是什麼](../../../02-prompt-engineering)
- [接下來的步驟](../../../02-prompt-engineering)

## 你將學到什麼

在上一個模組中，你了解了記憶如何促進對話式 AI，並使用 GitHub 模型進行基本互動。現在我們將專注於你如何提出問題 —— 也就是提示本身 —— 使用 Azure OpenAI 的 GPT-5.2。你如何結構化提示會劇烈影響所得到回應的品質。

我們會使用 GPT-5.2，因為它引入了推理控制 —— 你可以指示模型在回答前要思考多少。這讓不同的提示策略更為明顯，並幫助你了解何時使用每種方法。相比 GitHub 模型，Azure 對 GPT-5.2 的限制較少，我們也會從中受益。

## 先決條件

- 已完成模組 01（部署 Azure OpenAI 資源）
- 根目錄中有 `.env` 文件，包含 Azure 憑證（由模組 01 執行 `azd up` 時建立）

> **注意：** 如果你尚未完成模組 01，請先按照那裡的部署說明操作。

## 了解提示工程

提示工程是設計輸入文字，以穩定獲得你所需結果的技術。它不僅僅是提問，而是構造請求，讓模型精確理解你的需求以及該如何回應。

把它想像成給同事下指令。「修復錯誤」過於模糊。「在 UserService.java 第 45 行通過加入 null 檢查來修復空指標異常」很具體。語言模型的運作原理相同 —— 具體與結構化很重要。

## 如何使用 LangChain4j

本模組展示了使用先前模組相同 LangChain4j 基礎的高級提示模式，重點在提示結構與推理控制。

<img src="../../../translated_images/zh-HK/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4j 如何連接你的提示至 Azure OpenAI GPT-5.2*

**相依套件** — 模組 02 使用 `pom.xml` 中定義的以下 langchain4j 相依套件：
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel 配置** — [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

聊天模型以 Spring bean 手動配置，使用 OpenAI 官方客戶端，支援 Azure OpenAI 端點。與模組 01 的主要差異在於我們如何結構傳給 `chatModel.chat()` 的提示，而非模型本身的設置。

**系統與使用者訊息** — [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j 將訊息類型分開以提高清晰度。`SystemMessage` 設定 AI 的行為與上下文（例如「你是程式碼審查員」），而 `UserMessage` 包含實際請求。此分離讓你在不同使用者查詢間保持 AI 行為一致。

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/zh-HK/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage 提供持續上下文，UserMessages 則包含單獨的請求*

**多輪對話的 MessageWindowChatMemory** — 多輪對話模式重用模組 01 的 `MessageWindowChatMemory`。每個會話有自己的記憶實例，存於 `Map<String, ChatMemory>` 中，使多個同時會話無上下文混淆。

**提示模板** — 這次真正的重點是提示工程，而非新 LangChain4j API。每個模式（低熱情、高熱情、任務執行等）都使用相同的 `chatModel.chat(prompt)` 方法，但提示字串設計精心。XML 標籤、指示和格式全是提示文本的一部分，而非 LangChain4j 功能。

**推理控制** — GPT-5.2 的推理深度由提示中的指示控制，如「最多兩步推理」或「全面探索」。這是提示工程技巧，不是 LangChain4j 配置。庫只是負責將你的提示傳給模型。

要點是：LangChain4j 提供基礎設施（透過 [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java) 連接模型，和通過 [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 處理記憶與訊息），而本模組教你如何在此基礎上打造有效的提示。

## 核心模式

並非所有問題都用同一種方法。一些問題需要快答，一些則需深思。一些要顯示推理過程，有些只需要結果。此模組涵蓋八種提示模式 —— 每種為不同場景最佳化。你會實驗各種模式，學會何時用哪種方法最合適。

<img src="../../../translated_images/zh-HK/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八種提示工程模式及其使用案例概覽*

<img src="../../../translated_images/zh-HK/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*低熱情（快速、直接）vs 高熱情（全面、探索）推理方法比較*

**低熱情（快速且聚焦）** — 適合簡單問題，想要快速直接答案。模型最少推理，最多兩步。適用於計算、查詢或直接問題。

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **用 GitHub Copilot 探索：** 打開 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 詢問：
> - 「低熱情與高熱情提示模式有何不同？」
> - 「提示中的 XML 標籤如何幫助結構化 AI 回應？」
> - 「什麼時候該用自我反思模式，什麼時候用直接指令？」

**高熱情（深度且徹底）** — 適合複雜問題，需全面分析。模型全面探索並顯示詳細推理。用於系統設計、架構決策或複雜研究。

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任務執行（逐步進展）** — 適合多步驟工作流程。模型先做計畫，再逐步執行並敘述過程，最後給出總結。用於遷移、實作或其他多步流程。

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

思路鏈提示明確要求模型展示推理過程，提升複雜任務的準確度。逐步拆解有助於人與 AI 理解邏輯。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問此模式：
> - 「如何調整任務執行模式以應對長時間運行的操作？」
> - 「在生產應用中如何最佳結構化工具前言？」
> - 「如何捕捉並顯示 UI 中的中間進度更新？」

<img src="../../../translated_images/zh-HK/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*計畫 → 執行 → 總結的多步任務工作流程*

**自我反思代碼** — 用於生成生產級程式碼。模型生成代碼，依品質標準檢查並反覆改進。用於新功能或服務開發。

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

<img src="../../../translated_images/zh-HK/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*反覆改進循環——生成、評估、找問題、改進、重複*

**結構化分析** — 用於一致性評估。模型根據固定框架（正確性、慣例、效能、安全）審查程式碼。用於程式碼審查或質量評估。

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

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 詢問結構化分析：
> - 「如何為不同類型的程式碼審查自訂分析框架？」
> - 「如何程式化解析並處理結構化輸出？」
> - 「如何確保不同審查會話中嚴重程度的一致性？」

<img src="../../../translated_images/zh-HK/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*四類框架，帶嚴重程度用於一致程式碼審查*

**多輪對話** — 適合需要上下文的對話。模型記住前面訊息並承接。用於互動式協助或複雜問答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-HK/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*對話上下文如何隨多輪累積直至達到 token 限制*

**逐步推理** — 適合需顯示明確邏輯的問題。模型逐步展示推理過程。用於數學問題、邏輯謎題或需理解思考流程者。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-HK/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*問題分解成明確邏輯步驟*

**受限輸出** — 用於有特定格式需求的回應。模型嚴格遵守格式與長度規則。用於摘要或需精確輸出結構時。

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

<img src="../../../translated_images/zh-HK/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*強制執行格式、長度與結構要求*

## 使用現有 Azure 資源

**驗證部署：**

確保根目錄有 `.env` 文件，包含 Azure 憑證（模組 01 過程中建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已透過模組 01 的 `./start-all.sh` 啟動所有應用程式，這個模組已在 8083 端口執行。你可以跳過以下啟動指令，直接前往 http://localhost:8083。

**選項 1：使用 Spring Boot 面板（推薦 VS Code 使用者）**

開發容器包含 Spring Boot 面板擴展，提供管理所有 Spring Boot 應用的視覺介面。你可以在 VS Code 左側活動列看到（尋找 Spring Boot 圖標）。

透過 Spring Boot 面板，你可以：
- 檢視工作區內所有 Spring Boot 應用
- 一鍵啟動/停止應用
- 實時查看應用日誌
- 監控應用狀態

點擊「prompt-engineering」旁的播放按鈕啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動全部網頁應用（模組 01-04）：

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

或只啟動本模組：

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

兩個腳本會自動載入根目錄的 `.env` 環境變數，如果 JAR 檔案不存在，也會進行建置。

> **注意：** 若你想先手動建置所有模組再啟動：
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

打開瀏覽器，訪問 http://localhost:8083。

**停止應用：**

**Bash:**
```bash
./stop.sh  # 僅此模組
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

*主控台展示所有 8 種提示工程模式及其特性與使用場景*

## 探索模式

網頁介面讓你試驗多種提示策略。每種模式解決不同問題 —— 嘗試看看，了解何時各有長處。

### 低熱情 vs 高熱情

用低熱情模式問簡單問題，如「200 的 15% 是多少？」你會立即得到直接答案。用高熱情模式問複雜問題，如「為高流量 API 設計緩存策略」。你會看到模型放慢速度並提供詳盡推理。相同模型、相同問題結構 —— 但提示指示它思考多少。
<img src="../../../translated_images/zh-HK/low-eagerness-demo.898894591fb23aa0.webp" alt="低積極性示範" width="800"/>

*快速計算，推理最少*

<img src="../../../translated_images/zh-HK/high-eagerness-demo.4ac93e7786c5a376.webp" alt="高積極性示範" width="800"/>

*全面快取策略（2.8MB）*

### 工作執行（工具前置提示）

多步驟工作流程受益於事前規劃和進度解說。模型會概述它將要做什麼，敘述每個步驟，然後總結結果。

<img src="../../../translated_images/zh-HK/tool-preambles-demo.3ca4881e417f2e28.webp" alt="任務執行示範" width="800"/>

*建立具逐步解說的 REST 端點（3.9MB）*

### 自我反思程式碼

試試「建立一個電子郵件驗證服務」。模型不只生成程式碼然後停止，還會依品質標準評估、找出弱點並加以改進。你會看到它不斷迭代，直到程式碼達到生產標準。

<img src="../../../translated_images/zh-HK/self-reflecting-code-demo.851ee05c988e743f.webp" alt="自我反思程式碼示範" width="800"/>

*完整電子郵件驗證服務（5.2MB）*

### 結構化分析

程式碼審查需要一致的評估框架。模型使用固定的類別（正確性、實務、效能、安全性）及嚴重性等級進行分析。

<img src="../../../translated_images/zh-HK/structured-analysis-demo.9ef892194cd23bc8.webp" alt="結構化分析示範" width="800"/>

*基於框架的程式碼審查*

### 多輪對話

問「甚麼是 Spring Boot？」然後立即追問「給我一個範例」。模型會記得你的第一個問題，並專門提供 Spring Boot 的範例。若無記憶，第二個問題會太籠統。

<img src="../../../translated_images/zh-HK/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="多輪對話示範" width="800"/>

*問題間的上下文保存*

### 逐步推理

挑選一個數學問題，分別使用逐步推理和低積極性來嘗試。低積極性只給你答案 — 快速但不透明。逐步推理會顯示每個計算與決策。

<img src="../../../translated_images/zh-HK/step-by-step-reasoning-demo.12139513356faecd.webp" alt="逐步推理示範" width="800"/>

*附明確步驟的數學問題*

### 受限輸出

當你需要特定格式或字數時，這種模式強制嚴格遵守。試試生成一個剛好有 100 字、以重點條列的摘要。

<img src="../../../translated_images/zh-HK/constrained-output-demo.567cc45b75da1633.webp" alt="受限輸出示範" width="800"/>

*具有格式控制的機器學習摘要*

## 你真正學到的

**推理努力改變一切**

GPT-5.2 讓你透過提示掌控計算努力程度。低努力意味著快速回應且探索最少。高努力表示模型花時間深入思考。你學會根據問題複雜度調整努力 — 簡單問題不要浪費時間，複雜決策也不要匆促。

**結構引導行為**

注意提示中的 XML 標籤？它們不是裝飾。模型比起自由文字，更可靠地遵從結構化指令。當你需要多步驟流程或複雜邏輯時，結構幫助模型追蹤進度和後續工作。

<img src="../../../translated_images/zh-HK/prompt-structure.a77763d63f4e2f89.webp" alt="提示結構" width="800"/>

*具明確區段及 XML 風格組織的精良提示結構剖析*

**品質來自自我評估**

自我反思模式讓品質標準具體明確。不是默默期待模型「做對」，而是你明確告訴它「正確」是甚麼：邏輯正確、錯誤處理、效能、安全性。模型接著能評估自身輸出並改進。這讓程式碼生成變成穩定的流程，而非抽獎。

**上下文是有限的**

多輪對話透過每次請求附帶訊息歷史實現。但有字元上限 — 每個模型都有最大代幣數量。對話越長，你需要策略保持重要上下文而不超限。本單元教你記憶原理；日後你會學何時摘要、何時忘記、何時提取。

## 下一步

**下一單元：** [03-rag - RAG（檢索增強生成）](../03-rag/README.md)

---

**導覽：** [← 上一頁：模組 01 - 介紹](../01-introduction/README.md) | [返回主頁](../README.md) | [下一頁：模組 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力確保準確性，但請注意，自動翻譯可能存在錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議使用專業人工翻譯。我們不對因使用此翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->