# Module 00: 快速開始

## 目錄

- [介紹](../../../00-quick-start)
- [什麼是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依賴](../../../00-quick-start)
- [先決條件](../../../00-quick-start)
- [設置](../../../00-quick-start)
  - [1. 取得您的 GitHub 令牌](../../../00-quick-start)
  - [2. 設定您的令牌](../../../00-quick-start)
- [運行範例](../../../00-quick-start)
  - [1. 基本聊天](../../../00-quick-start)
  - [2. 提示模式](../../../00-quick-start)
  - [3. 函式呼叫](../../../00-quick-start)
  - [4. 文件問答（簡易 RAG）](../../../00-quick-start)
  - [5. 負責任的 AI](../../../00-quick-start)
- [每個範例展示的內容](../../../00-quick-start)
- [下一步](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 介紹

此快速入門旨在讓您盡快開始使用 LangChain4j。它涵蓋使用 LangChain4j 和 GitHub 模型構建 AI 應用的基本知識。在接下來的模組中，您將轉向使用 Azure OpenAI 和 GPT-5.2，並深入探討每個概念。

## 什麼是 LangChain4j？

LangChain4j 是一個 Java 庫，簡化了 AI 驅動應用的構建。您不需要處理 HTTP 客戶端和 JSON 解析，而只需使用乾淨的 Java API。

LangChain 中的“鏈”指的是將多個組件連接起來——您可以將提示鏈接到模型，再鏈接到解析器，或將多個 AI 調用串聯，其中一個輸出成為下一個輸入。本快速開始聚焦於基礎知識，之後會探索更複雜的鏈。

<img src="../../../translated_images/zh-MO/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中的組件鏈接 — 組件積木連接起來，創造強大的 AI 工作流程*

我們將使用三個核心組件：

**ChatModel** — AI 模型交互的介面。呼叫 `model.chat("prompt")` 並得到回應字串。我們使用 `OpenAiOfficialChatModel`，它可與 OpenAI 兼容的端點（如 GitHub 模型）配合使用。

**AiServices** — 建立類型安全的 AI 服務介面。定義方法，使用 `@Tool` 標註，LangChain4j 負責協調。AI 在需要時會自動呼叫您的 Java 方法。

**MessageWindowChatMemory** — 維護對話歷史。沒有它，每次請求都是獨立的。有了它，AI 會記住之前的訊息，在多輪對話中保持上下文。

<img src="../../../translated_images/zh-MO/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架構 — 核心組件協同工作，驅動您的 AI 應用*

## LangChain4j 依賴

此快速開始使用了 [`pom.xml`](../../../00-quick-start/pom.xml) 中的三個 Maven 依賴：

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

`langchain4j-open-ai-official` 模組提供了 `OpenAiOfficialChatModel` 類，用於連接 OpenAI 兼容的 API。GitHub 模型使用相同的 API 格式，因此不需要特殊的適配器——只需設定基底 URL 為 `https://models.github.ai/inference`。

`langchain4j-easy-rag` 模組提供自動文件切割、嵌入和檢索功能，讓您在不需手動配置每個步驟的情況下構建 RAG 應用。

## 先決條件

**使用開發容器？** 已預裝 Java 和 Maven，您只需一個 GitHub 個人存取令牌。

**本地開發：**
- Java 21+、Maven 3.9+
- GitHub 個人存取令牌（以下有指示）

> **注意：** 本模組使用來自 GitHub 模型的 `gpt-4.1-nano`。請勿修改程式碼中的模型名稱——此名稱已設定為與 GitHub 可用模型相容。

## 設置

### 1. 取得您的 GitHub 令牌

1. 前往 [GitHub 設定 → 個人存取令牌](https://github.com/settings/personal-access-tokens)
2. 點擊「產生新令牌」
3. 設定一個描述性名稱（例如「LangChain4j Demo」）
4. 設定過期時間（建議 7 天）
5. 在「帳戶權限」中找到「Models」，設為「唯讀」
6. 點擊「產生令牌」
7. 複製並保存您的令牌——之後將無法再次查看

### 2. 設定您的令牌

**選項一：使用 VS Code（推薦）**

如果您使用 VS Code，請將令牌加入專案根目錄下的 `.env` 文件：

若無 `.env` 檔案，請將 `.env.example` 複製為 `.env`，或在專案根目錄建立一個新的 `.env` 文件。

**範例 `.env` 檔案：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

之後您只需在檔案總管右鍵點擊任何示範檔案（如 `BasicChatDemo.java`），並選擇 **"Run Java"**，或使用「執行與除錯」面板中的啟動配置。

**選項二：使用終端機**

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

**使用 VS Code:** 只需在檔案總管右鍵點擊任何示範檔案，選擇 **"Run Java"**，或使用「執行與除錯」面板的啟動配置（請先將令牌加入 `.env` 文件）。

**使用 Maven:** 您也可以從命令行運行：

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

示範零樣本、少樣本、思維鏈及角色基礎提示。

### 3. 函式呼叫

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI 在需要時會自動呼叫您的 Java 方法。

### 4. 文件問答（簡易 RAG）

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

使用簡易 RAG 以自動嵌入與檢索方式，根據您的文件提問。

### 5. 負責任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

查看 AI 安全篩選如何阻擋有害內容。

## 每個範例展示的內容

**基本聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

從這裡開始看看最簡單的 LangChain4j 用法。您將建立一個 `OpenAiOfficialChatModel`，用 `.chat()` 傳送提示並收到回應。這展示了基礎：如何用自訂端點和 API 鍵初始化模型。理解此模式後，其它所有內容都基於此。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 開啟 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)，並詢問：
> - 「我如何將此程式碼從 GitHub 模型切換到 Azure OpenAI？」
> - 「在 OpenAiOfficialChatModel.builder() 中還可以設定哪些參數？」
> - 「如何新增串流回應，而不是等完整回應？」

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

既然您已學會如何與模型對話，接下來看看怎麼說。此範例使用相同模型設定，但展示五種不同的提示模式。試試零樣本提示進行直接指令、少樣本提示學習範例、思維鏈提示揭露推理步驟，以及角色基礎提示設定上下文。您將看到，同一模型根據提示方式呈現截然不同的結果。

範例還展示提示模板，它能用變數創建可重用提示。
以下示例使用 LangChain4j 的 `PromptTemplate` 填入變數。AI 將根據給定的目的地和活動回答。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 開啟 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)，並詢問：
> - 「零樣本和少樣本提示有何差別？何時應該用哪種？」
> - 「溫度參數如何影響模型回應？」
> - 「有哪些技術能防止生產環境的提示注入攻擊？」
> - 「如何為常用模式建立可重用的 PromptTemplate 物件？」

**工具整合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

這裡展現了 LangChain4j 的強大功能。您將使用 `AiServices` 建立可呼叫您 Java 方法的 AI 助手。只需用 `@Tool("說明")` 標註方法，LangChain4j 會處理其餘——AI 會根據使用者需求自動決定何時使用每個工具。這展示函式呼叫，是構建能採取行動，而不僅僅回答問題的 AI 的關鍵技巧。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 開啟 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，並詢問：
> - 「@Tool 標註如何運作？LangChain4j 背後做了什麼？」
> - 「AI 能否依序呼叫多個工具來解決複雜問題？」
> - 「如果工具丟出例外，要如何處理錯誤？」
> - 「如何整合真實 API，而不是這個計算機示例？」

**文件問答（簡易 RAG）** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

您將看到使用 LangChain4j「簡易 RAG」方法的檢索強化生成。文件被載入、自動切割並嵌入至記憶體存儲，再由內容檢索器於查詢時提供關聯片段給 AI。AI 回答基於您的文件，不是它的一般知識。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 開啟 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)，並詢問：
> - 「RAG 如何防止 AI 產生錯誤，而不是依賴模型訓練資料？」
> - 「這種簡易做法與自訂 RAG 流程有何不同？」
> - 「如何擴充以處理多份文件或更大的知識庫？」

**負責任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

建構深度防禦的 AI 安全。此示範展示兩層保護如何協同運作：

**第一部分：LangChain4j 輸入護欄** — 阻擋危險提示，防止它們送入 LLM。建立自訂護欄，檢查禁止關鍵字或模式。這些在您的程式碼中執行，速度快且免費。

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

**第二部分：供應商安全過濾** — GitHub 模型內建過濾器，捕捉護欄可能漏掉的內容。您會看到嚴重違規時的硬拒絕（HTTP 400 錯誤）以及軟拒絕，AI 禮貌地拒絕回答。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 開啟 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)，並詢問：
> - 「什麼是 InputGuardrail？如何建立自己的？」
> - 「硬拒絕和軟拒絕有什麼差別？」
> - 「為什麼要同時使用護欄和供應商過濾？」

## 下一步

**下一模組：** [01-introduction - LangChain4j 快速入門](../01-introduction/README.md)

---

**導航：** [← 返回主頁](../README.md) | [下一步：Module 01 - 介紹 →](../01-introduction/README.md)

---

## 故障排除

### 首次 Maven 建置

**問題：** 初次執行 `mvn clean compile` 或 `mvn package` 花費較長時間（10-15 分鐘）

**原因：** Maven 首次建置時需下載所有專案依賴（Spring Boot、LangChain4j 函式庫、Azure SDK 等）。

**解決方式：** 這是正常行為。後續建置速度會快很多，因為依賴已緩存在本機。下載時間視網絡速度而定。

### PowerShell 下的 Maven 指令語法

**問題：** Maven 指令失敗，錯誤為 `Unknown lifecycle phase ".mainClass=..."`
**原因**：PowerShell 將 `=` 解釋為變數指派運算子，導致 Maven 屬性語法錯誤

**解決方法**：在 Maven 指令前使用停止解析運算子 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 運算子告知 PowerShell 將後續所有參數直接傳遞給 Maven，不進行解析。

### Windows PowerShell Emoji 顯示問題

**問題**：AI 回應在 PowerShell 中顯示亂碼（例如 `????` 或 `â??`）而非表情符號

**原因**：PowerShell 預設編碼不支援 UTF-8 表情符號

**解決方法**：在執行 Java 應用程式前執行此指令：
```cmd
chcp 65001
```

此指令強制終端機採用 UTF-8 編碼。或者，可以使用對 Unicode 支援較佳的 Windows Terminal。

### API 呼叫除錯

**問題**：AI 模型出現身份驗證錯誤、速率限制，或非預期回應

**解決方法**：範例中包含了 `.logRequests(true)` 和 `.logResponses(true)`，用於在主控台顯示 API 呼叫，有助追蹤身份驗證錯誤、速率限制或非預期回應。正式環境中請移除這些標記以減少日誌干擾。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力確保翻譯的準確性，但請注意，機器自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們對因使用本翻譯而產生的任何誤解或誤釋不承擔任何責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->