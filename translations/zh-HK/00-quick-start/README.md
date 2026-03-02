# Module 00: 快速開始

## 目錄

- [簡介](../../../00-quick-start)
- [什麼是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依賴項](../../../00-quick-start)
- [先決條件](../../../00-quick-start)
- [設置](../../../00-quick-start)
  - [1. 取得您的 GitHub 令牌](../../../00-quick-start)
  - [2. 設置您的令牌](../../../00-quick-start)
- [運行範例](../../../00-quick-start)
  - [1. 基本聊天](../../../00-quick-start)
  - [2. 提示模式](../../../00-quick-start)
  - [3. 函數調用](../../../00-quick-start)
  - [4. 文件問答 (Easy RAG)](../../../00-quick-start)
  - [5. 負責任的 AI](../../../00-quick-start)
- [每個範例展示什麼](../../../00-quick-start)
- [下一步](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 簡介

此快速入門旨在讓您盡快開始使用 LangChain4j。它涵蓋了使用 LangChain4j 和 GitHub 模型構建 AI 應用程序的絕對基礎。在接下來的模塊中，您將切換到 Azure OpenAI 和 GPT-5.2，並深入探討每個概念。

## 什麼是 LangChain4j？

LangChain4j 是一個簡化構建 AI 驅動應用程式的 Java 庫。您不需要處理 HTTP 客戶端和 JSON 解析，而是使用清晰的 Java API。

LangChain 中的「鏈」是指將多個組件鏈接起來——您可能會將提示鏈接到模型，再鏈接到解析器，或者將多個 AI 調用連接起來，其中一個輸出用於下一個輸入。這個快速入門聚焦於基礎，在此基礎上再探索更複雜的鏈。

<img src="../../../translated_images/zh-HK/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中的鏈接組件 - 建立連接創造強大 AI 工作流程的積木*

我們將使用三個核心組件：

**ChatModel** — AI 模型互動的介面。呼叫 `model.chat("prompt")` 並獲得回應字串。我們使用 `OpenAiOfficialChatModel`，它支援類 OpenAI 端點，如 GitHub 模型。

**AiServices** — 創建類型安全的 AI 服務介面。定義方法，使用 `@Tool` 註解它們，LangChain4j 負責協調。AI 會在需要時自動呼叫您的 Java 方法。

**MessageWindowChatMemory** — 維護對話歷史。沒有它，每次請求都是獨立的。使用它，AI 會記住先前訊息並跨多輪對話維持上下文。

<img src="../../../translated_images/zh-HK/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架構 - 核心組件協作，為您的 AI 應用提供動力*

## LangChain4j 依賴項

本快速入門在 [`pom.xml`](../../../00-quick-start/pom.xml) 中使用三個 Maven 依賴：

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official` 模組提供 `OpenAiOfficialChatModel` 類，用於連接 OpenAI 兼容的 API。GitHub 模型使用相同的 API 格式，因此不需要特別的適配器——只需將基礎 URL 指向 `https://models.github.ai/inference`。

`langchain4j-easy-rag` 模組提供自動文件拆分、嵌入和檢索功能，讓您無需手動配置每個步驟即可構建 RAG 應用。

## 先決條件

**使用開發容器嗎？** Java 和 Maven 已安裝好，您只需要 GitHub 個人訪問令牌。

**本地開發：**
- Java 21+，Maven 3.9+
- GitHub 個人訪問令牌（以下說明）

> **注意：** 此模塊使用 GitHub 模型的 `gpt-4.1-nano`。請勿修改程式碼中的模型名稱——已配置以適配 GitHub 可用模型。

## 設置

### 1. 取得您的 GitHub 令牌

1. 前往 [GitHub 設定 → 個人訪問令牌](https://github.com/settings/personal-access-tokens)
2. 點選「生成新令牌」
3. 設置描述名稱（例如：「LangChain4j Demo」）
4. 設置令牌有效期（建議 7 天）
5. 在「帳戶許可權」下找到「Models」並設為「唯讀」
6. 點選「生成令牌」
7. 複製並保存令牌——之後無法再次查看

### 2. 設置您的令牌

**方案一：使用 VS Code（推薦）**

如果您使用 VS Code，將令牌新增到專案根目錄的 `.env` 文件中：

如果 `.env` 文件不存在，請複製 `.env.example` 為 `.env`，或在專案根目錄新建一個 `.env` 文件。

**示例 `.env` 文件：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

然後您只需在資源管理器中右鍵點擊任何示範檔案（例如 `BasicChatDemo.java`），選擇 **「Run Java」**，或使用「執行與偵錯」面板中的啟動配置執行。

**方案二：使用終端機**

將令牌設為環境變數：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 運行範例

**使用 VS Code：** 只需在資源管理器中右鍵點擊任何示範檔案並選擇 **「Run Java」**，或使用「執行與偵錯」面板的啟動配置（確保您先將令牌加到 `.env` 文件）。

**使用 Maven：** 您也可以從命令列執行：

### 1. 基本聊天

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. 提示模式

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

展示零樣本（zero-shot）、少樣本（few-shot）、思維鏈（chain-of-thought）和角色基礎提示。

### 3. 函數調用

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI 會在需要時自動呼叫您的 Java 方法。

### 4. 文件問答 (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

使用 Easy RAG 的自動嵌入和檢索功能，針對文件提問。

### 5. 負責任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

查看 AI 如何透過安全過濾器阻擋有害內容。

## 每個範例展示什麼

**基本聊天** — [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

從這裡開始了解 LangChain4j 最簡單的使用方式。您將創建一個 `OpenAiOfficialChatModel`，使用 `.chat()` 傳送提示並獲取回應。這展示了基礎：如何使用自定義端點和 API 金鑰初始化模型。理解此模式後，一切更複雜的用法均建立在此之上。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試看：** 打開 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)，問：
> - 「我如何在此程式碼中從 GitHub 模型切換到 Azure OpenAI？」
> - 「OpenAiOfficialChatModel.builder() 可以配置哪些其他參數？」
> - 「如何新增串流回應，而不是等待完整回應？」

**提示工程** — [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

您已知道如何與模型交談，現在讓我們深入探討您對模型說什麼。此示範使用相同模型設定，展示五種不同的提示模式。嘗試零樣本提示以直接指令，少樣本提示學習範例，思維鏈提示顯示推理步驟，角色基礎提示設定上下文。您將看到同一模型因提示方式不同而產生截然不同的結果。

示範中還展示了提示模板，這是創建帶變數可重用提示的強大方式。下例展示如何使用 LangChain4j `PromptTemplate` 填充變數。AI 將根據指定的目的地和活動回答。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試看：** 打開 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)，問：
> - 「零樣本與少樣本提示有什麼差異？何時應該使用哪種？」
> - 「溫度參數如何影響模型回答？」
> - 「有哪些方法可防止生產環境中的提示注入攻擊？」
> - 「如何為常見模式創建可重用的 PromptTemplate 物件？」

**工具整合** — [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

這是 LangChain4j 強大的地方。您將使用 `AiServices` 創建 AI 助手來呼叫您的 Java 方法。只需用 `@Tool("描述")` 註解方法，LangChain4j 負責其他工作—AI 會根據使用者的問題自動決定何時使用哪個工具。這展示了函數調用，是打造能採取行動而非僅回答問題的 AI 的關鍵技巧。

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試看：** 打開 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，問：
> - 「@Tool 註解是如何運作的？LangChain4j 背後做了什麼？」
> - 「AI 能串接多個工具來解決複雜問題嗎？」
> - 「如果工具拋出異常，該如何處理錯誤？」
> - 「如何將此計算器示例換成呼叫真實 API？」

**文件問答 (Easy RAG)** — [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

這裡展示了使用 LangChain4j 「Easy RAG」 方法的 RAG（檢索增強生成）。文件被加載，自動拆分並嵌入記憶體資料庫，查詢時由內容檢索器供給相關片段給 AI。AI 的回答基於您的文件，而非其一般知識。

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試看：** 打開 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)，問：
> - 「RAG 如何防止 AI 幻覺，與使用模型訓練數據相比有何不同？」
> - 「這個簡易方法與自訂 RAG 管線有什麼差異？」
> - 「如何擴展以處理多個文件或更大知識庫？」

**負責任的 AI** — [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

建立縝密的 AI 安全防護。此示範展示兩層保護合力發揮效用：

**第 1 部分：LangChain4j 輸入守衛** — 在請求到達 LLM 前阻擋危險提示。建立自訂守衛，檢查禁止關鍵詞或模式。它們在您的程式碼中運行，因此快速且免費。

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**第 2 部分：供應商安全過濾器** — GitHub 模型有內建過濾器，可捕捉守衛可能錯過的內容。您將看到嚴重違規的硬性封鎖（HTTP 400 錯誤）與 AI 委婉拒絕的軟性拒絕。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試看：** 打開 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)，問：
> - 「什麼是 InputGuardrail？如何自行製作？」
> - 「硬性阻擋與軟性拒絕有何差別？」
> - 「為什麼要同時使用守衛和供應商過濾器？」

## 下一步

**下一模組：** [01-introduction - LangChain4j 入門](../01-introduction/README.md)

---

**導覽：** [← 返回主頁](../README.md) | [下一步：模組 01 - 簡介 →](../01-introduction/README.md)

---

## 故障排除

### 第一次 Maven 編譯

**問題：** 初次執行 `mvn clean compile` 或 `mvn package` 時，耗時很長（10-15 分鐘）

**原因：** Maven 需要在第一次編譯時下載所有專案依賴（Spring Boot、LangChain4j 函式庫、Azure SDK 等）。

**解決方案：** 這是正常行為。之後的編譯會快得多，因為依賴已快取至本地。下載速度取決於您的網絡品質。

### PowerShell Maven 命令語法

**問題：** Maven 命令執行失敗，出現錯誤 `Unknown lifecycle phase ".mainClass=..."`
**原因**：PowerShell 將 `=` 解釋為變量賦值運算符，導致 Maven 屬性語法出錯

**解決方法**：在 Maven 命令前使用停止解析運算符 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 運算符告訴 PowerShell 將所有剩餘參數以字面形式傳遞給 Maven，無需解析。

### Windows PowerShell Emoji 顯示

**問題**：AI 回應在 PowerShell 中顯示亂碼（例如 `????` 或 `â??`）而非表情符號

**原因**：PowerShell 預設編碼不支援 UTF-8 表情符號

**解決方法**：執行 Java 應用程式前先運行此命令：
```cmd
chcp 65001
```

這會強制終端使用 UTF-8 編碼。或者，使用 Windows Terminal，其 Unicode 支援更佳。

### 偵錯 API 呼叫

**問題**：AI 模型驗證失敗、速率限制或非預期回應

**解決方法**：範例中包含 `.logRequests(true)` 和 `.logResponses(true)`，會在控制台顯示 API 呼叫，方便排查驗證錯誤、頻率限制或非預期回應。生產環境中移除此設定以減少日誌雜訊。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們致力於準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用此翻譯而引起的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->