# Module 00: 快速開始

## 目錄

- [介紹](../../../00-quick-start)
- [什麼是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依賴](../../../00-quick-start)
- [先決條件](../../../00-quick-start)
- [設定](../../../00-quick-start)
  - [1. 取得你的 GitHub 代幣](../../../00-quick-start)
  - [2. 設定你的代幣](../../../00-quick-start)
- [執行範例](../../../00-quick-start)
  - [1. 基本聊天](../../../00-quick-start)
  - [2. 提示範例](../../../00-quick-start)
  - [3. 函數呼叫](../../../00-quick-start)
  - [4. 文件問答（Easy RAG）](../../../00-quick-start)
  - [5. 負責任的 AI](../../../00-quick-start)
- [各範例說明](../../../00-quick-start)
- [下一步](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 介紹

本快速開始旨在幫助你盡快使用 LangChain4j 起步。它涵蓋使用 LangChain4j 和 GitHub 模型建立 AI 應用程序的基本知識。在後續模組中，你將使用 Azure OpenAI 與 LangChain4j 一起建立更進階的應用。

## 什麼是 LangChain4j？

LangChain4j 是一個 Java 函式庫，簡化了建造 AI 驅動應用的流程。你不用再處理 HTTP 用戶端及 JSON 解析，而是使用乾淨的 Java API。

LangChain 中的「chain」指的是將多個元件串連起來 — 你可能會將提示串接到模型再到解析器，或者串起多個 AI 呼叫，其中一個輸出成為下一個輸入。本快速開始先聚焦基本原理，之後會探索更複雜的串鏈。

<img src="../../../translated_images/zh-MO/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中的組件串接 — 建構塊相連以創造強大的 AI 工作流程*

我們將使用三個核心元件：

**ChatModel** — 與 AI 模型交互的介面。呼叫 `model.chat("prompt")` 即可取得回應字串。我們使用 `OpenAiOfficialChatModel`，支援像 GitHub Models 這樣兼容 OpenAI 的端點。

**AiServices** — 建立類型安全的 AI 服務介面。定義方法，並用 `@Tool` 做註解，LangChain4j 自動處理調度。AI 會在需要時自動呼叫你的 Java 方法。

**MessageWindowChatMemory** — 維護對話歷史。沒有它，每次請求都是獨立的；有了它，AI 能記住先前訊息並維持多輪上下文。

<img src="../../../translated_images/zh-MO/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架構 — 核心元件協同運作，為你的 AI 應用提供動力*

## LangChain4j 依賴

本快速開始使用 [`pom.xml`](../../../00-quick-start/pom.xml) 中的三個 Maven 依賴：

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

`langchain4j-open-ai-official` 模組提供 `OpenAiOfficialChatModel` 類，連接到兼容 OpenAI 的 API。GitHub Models 使用相同的 API 格式，所以不需要特別適配器 — 只需將基礎 URL 指向 `https://models.github.ai/inference`。

`langchain4j-easy-rag` 模組提供自動文件拆分、嵌入及檢索，讓你不用手動配置，就能建造 RAG 應用。

## 先決條件

**使用 Dev Container？** Java 和 Maven 已預先安裝。你只需要一個 GitHub 個人存取代幣。

**本地開發：**
- Java 21+、Maven 3.9+
- GitHub 個人存取代幣（步驟如下）

> **注意：** 本模組使用 GitHub Models 的 `gpt-4.1-nano`。請勿更改程式碼中的模型名稱 — 它已設定以對應 GitHub 可用模型。

## 設定

### 1. 取得你的 GitHub 代幣

1. 前往 [GitHub 設定 → 個人存取代幣](https://github.com/settings/personal-access-tokens)
2. 點擊「產生新代幣」
3. 設定描述名稱（例如「LangChain4j Demo」）
4. 設定有效期限（建議 7 天）
5. 在「帳戶權限」中找到「Models」，設定為「只讀」
6. 點擊「產生代幣」
7. 複製並保存代幣 - 之後無法再看到

### 2. 設定你的代幣

**選項 1：使用 VS Code（建議）**

若你使用 VS Code，請將代幣加入專案根目錄的 `.env` 檔案：

如果 `.env` 檔案不存在，請複製 `.env.example` 並改為 `.env`，或在專案根目錄新建 `.env`。

**範例 `.env` 檔案：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

接著，你可以在 Explorer 中對任何範例檔案（如 `BasicChatDemo.java`）點擊右鍵並選擇 **「Run Java」**，或使用執行與偵錯面板的啟動設定。

**選項 2：使用終端機**

將代幣設為環境變數：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 執行範例

**使用 VS Code：** 在 Explorer 中直接對範例檔案點擊右鍵選擇 **「Run Java」**，或使用執行與偵錯面板的啟動設定（確保已先將代幣加入 `.env` 檔）。

**使用 Maven：** 你也可從命令列執行：

### 1. 基本聊天

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. 提示範例

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

展示零次學習、少次學習、思維鏈及角色提示。

### 3. 函數呼叫

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI 在需要時自動呼叫你的 Java 方法。

### 4. 文件問答（Easy RAG）

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

使用 Easy RAG 和自動嵌入及檢索，針對你的文件提出問題。

### 5. 負責任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

展示 AI 安全篩選如何阻擋有害內容。

## 各範例說明

**基本聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

從這裡開始看 LangChain4j 的最基礎用法。你會建立一個 `OpenAiOfficialChatModel`，用 `.chat()` 傳入提示並取得回應。它示範了基礎：如何用自訂端點和 API 金鑰初始化模型。理解這個模式後，其他都基於此展開。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat 訊息：** 開啟 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)，問：
> - 「如何把程式碼裡的 GitHub Models 換成 Azure OpenAI？」
> - 「OpenAiOfficialChatModel.builder() 還能配置哪些其他參數？」
> - 「怎麼加入串流回應，不用等待完整回答？」

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

既然知道如何和模型互動，來看看怎麼說給它聽。此示範使用相同模型設定，但展示五種不同的提示範例。試試零次學習給指令、少次學習從範例學習、思維鏈揭露推理過程，或角色設定定義上下文。你會發現同一模型根據框架不同，結果差異極大。

示範還包含提示模板，這是建立帶變數的可重用提示的強大方法。
以下範例示範用 LangChain4j 的 `PromptTemplate` 填充變數，AI 將根據提供的目的地和活動回答。

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

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat 訊息：** 開啟 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)，問：
> - 「零次學習和少次學習提示差在哪裡？各自適合什麼情境？」
> - 「temperature 參數怎麼影響模型回應？」
> - 「防止提示注入攻擊有哪些實務技巧？」
> - 「怎麼建立可重用的 PromptTemplate 物件？」

**工具整合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

這是 LangChain4j 強大的地方。你會用 `AiServices` 建立能呼叫你 Java 方法的 AI 助手。只要用 `@Tool("說明")` 標註方法，LangChain4j 會負責整合調度 — AI 會根據使用者需求，自動決定何時使用哪個工具。這示範了函數呼叫，是建構可實作動作 AI 的關鍵技術，不只是回答問題。

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

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat 訊息：** 開啟 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，問：
> - 「@Tool 註解怎麼運作？LangChain4j 背後做了哪些事？」
> - 「AI 能順序呼叫多個工具解決複雜問題嗎？」
> - 「如果工具拋出例外，該怎麼處理錯誤？」
> - 「如何整合真實 API，而不是這個計算器範例？」

**文件問答（Easy RAG）** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

這裡看到利用 LangChain4j 的「Easy RAG」方法的檢索強化生成（RAG）。文件被載入、自動拆分並嵌入至記憶體存儲，再由內容檢索器在查詢時提供相關片段給 AI。AI 是依據你的文件，而非一般知識作答。

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

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat 訊息：** 開啟 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)，問：
> - 「RAG 如何比模型訓練資料避免 AI 產生幻覺？」
> - 「簡易方法和自訂 RAG 流程差異在哪裡？」
> - 「如何擴展支援多份文件或較大型知識庫？」

**負責任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

建立多重防線的 AI 安全。本示範顯示兩層保護互相配合：

**第一部分：LangChain4j 輸入護欄** — 在到達 LLM 前阻擋危險提示。建置自訂護欄，檢查禁止字詞或模式。它們在程式內運行，因此快速且免費。

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

**第二部分：提供者安全篩選** — GitHub Models 有內建篩選機制，捕捉護欄可能漏掉的問題。你會看到嚴重違規時的硬性封鎖（HTTP 400 錯誤）和軟性拒絕，即 AI 有禮貌地拒絕回答。

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat 訊息：** 開啟 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)，問：
> - 「什麼是 InputGuardrail？我要怎麼建立自己的？」
> - 「硬性封鎖和軟性拒絕有何不同？」
> - 「為什麼要同時使用護欄和提供者篩選？」

## 下一步

**下一模組：** [01-introduction - LangChain4j 與 Azure 上的 gpt-5 入門](../01-introduction/README.md)

---

**導航：** [← 返回主頁](../README.md) | [下一頁：模組 01 - 介紹 →](../01-introduction/README.md)

---

## 故障排除

### 首次 Maven 建置

**問題：** 初次使用 `mvn clean compile` 或 `mvn package` 執行耗時較長（10-15 分鐘）

**原因：** Maven 需要在首次建置時下載所有專案依賴（Spring Boot、LangChain4j 函式庫、Azure SDK 等）。

**解決方案：** 這是正常現象。之後建置因本地已快取依賴會顯著加快。下載速度取決於你的網路連線。

### PowerShell Maven 指令語法

**問題：** Maven 指令執行失敗，報錯 `Unknown lifecycle phase ".mainClass=..."`
**原因**：PowerShell 將 `=` 解釋為變數賦值運算符，導致 Maven 屬性語法錯誤

**解決方案**：在 Maven 命令前使用停止解析運算符 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 運算符告訴 PowerShell 將所有後續參數原樣傳遞給 Maven，無需解釋。

### Windows PowerShell 表情符號顯示

**問題**：AI 回應中在 PowerShell 顯示為亂碼（例如 `????` 或 `â??`）而非表情符號

**原因**：PowerShell 預設編碼不支援 UTF-8 表情符號

**解決方案**：在執行 Java 應用程式前，執行此命令：
```cmd
chcp 65001
```

這會強制終端使用 UTF-8 編碼。或者，您也可以使用 Unicode 支援更佳的 Windows Terminal。

### 除錯 API 呼叫

**問題**：來自 AI 模型的身份驗證錯誤、速率限制或意外回應

**解決方案**：範例包含 `.logRequests(true)` 和 `.logResponses(true)`，可在控制台顯示 API 呼叫，有助於排查身份驗證錯誤、速率限制或意外回應。生產環境中請移除這些標記以減少日誌噪音。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力確保翻譯的準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於關鍵資訊，建議使用專業人工翻譯。我們對因使用本翻譯而引起的任何誤解或誤譯不承擔任何責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->