# Module 00: 快速入門

## 目錄

- [介紹](../../../00-quick-start)
- [什麼是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依賴](../../../00-quick-start)
- [先決條件](../../../00-quick-start)
- [設定](../../../00-quick-start)
  - [1. 取得你的 GitHub 令牌](../../../00-quick-start)
  - [2. 設定你的令牌](../../../00-quick-start)
- [執行範例](../../../00-quick-start)
  - [1. 基本聊天](../../../00-quick-start)
  - [2. 提示範例](../../../00-quick-start)
  - [3. 函式呼叫](../../../00-quick-start)
  - [4. 文件問答 (簡易 RAG)](../../../00-quick-start)
  - [5. 負責任的 AI](../../../00-quick-start)
- [每個範例展示的內容](../../../00-quick-start)
- [後續步驟](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 介紹

此快速入門旨在讓你盡快開始使用 LangChain4j。它涵蓋使用 LangChain4j 和 GitHub 模型構建 AI 應用程式的基本知識。在後續模組中，你將使用 Azure OpenAI 搭配 LangChain4j 來構建更進階的應用程式。

## 什麼是 LangChain4j？

LangChain4j 是一個簡化構建 AI 應用程式的 Java 庫。你不需要處理 HTTP 用戶端和 JSON 解析，只需使用乾淨的 Java API。

LangChain 中的「鏈」指的是將多個元件串接起來 —— 你可以將提示串接到模型，再串接到解析器，或者串接多個 AI 呼叫，讓一個輸出成為下一個輸入。此快速入門聚焦基礎，後面再探討更複雜的鏈結。

<img src="../../../translated_images/zh-HK/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中元件鏈結 —— 構件連接以打造強大的 AI 工作流程*

我們將使用三個核心元件：

**ChatModel** — AI 模型互動的介面。呼叫 `model.chat("prompt")` 並取得回應字串。我們使用 `OpenAiOfficialChatModel` ，它支援與 OpenAI 相容的端點，如 GitHub 模型。

**AiServices** — 用來建立型別安全的 AI 服務介面。定義方法並用 `@Tool` 標註，LangChain4j 負責協調調度。AI 會在需要時自動呼叫你的 Java 方法。

**MessageWindowChatMemory** — 維護對話歷史。沒有此元件時，每次請求都是獨立的；有它後，AI 會記得先前的訊息，維持多輪對話的上下文。

<img src="../../../translated_images/zh-HK/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架構 —— 核心元件協同運作，推動你的 AI 應用*

## LangChain4j 依賴

此快速入門在 [`pom.xml`](../../../00-quick-start/pom.xml) 中使用了三個 Maven 依賴：

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

`langchain4j-open-ai-official` 模組提供連接 OpenAI 相容 API 的 `OpenAiOfficialChatModel` 類別。GitHub 模型採用相同的 API 格式，無需特別適配，只需將基底 URL 指向 `https://models.github.ai/inference` 即可。

`langchain4j-easy-rag` 模組提供自動文件切分、嵌入與檢索功能，讓你可建立 RAG 應用，無需手動設定每步驟。

## 先決條件

**使用開發容器嗎？** Java 和 Maven 已安裝，僅需 GitHub 個人存取令牌。

**本地開發:**
- Java 21+，Maven 3.9+
- GitHub 個人存取令牌（以下有詳情）

> **注意：** 本模組使用 GitHub 模型的 `gpt-4.1-nano`。請勿修改程式碼中的模型名稱 —— 它已設定為符合 GitHub 可用模型。

## 設定

### 1. 取得你的 GitHub 令牌

1. 前往 [GitHub 設定 → 個人存取令牌](https://github.com/settings/personal-access-tokens)
2. 點選「生成新令牌」
3. 設定描述性名稱（例如 "LangChain4j Demo"）
4. 設定有效期限（建議 7 天）
5. 在「帳戶權限」中找到「Models」並設為「只讀」
6. 按「生成令牌」
7. 複製並保存你的令牌 —— 後續無法再次查看

### 2. 設定你的令牌

**方案 1：使用 VS Code（建議）**

如果使用 VS Code，將令牌加入專案根目錄的 `.env` 檔案：

如果 `.env` 檔案不存在，請複製 `.env.example` 為 `.env`，或自行在專案根目錄建立新的 `.env` 檔。

**範例 `.env` 檔：**
```bash
# 係 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

之後你可在檔案總管中，右鍵任意範例檔案（例如 `BasicChatDemo.java`），選擇 **"Run Java"** ，或在「執行與除錯」面板使用啟動配置。

**方案 2：使用終端機**

設定 Token 為環境變數：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 執行範例

**使用 VS Code：** 在檔案總管中右鍵任意範例檔案，選擇 **"Run Java"**，或於「執行與除錯」面板使用啟動配置（確保先將令牌加入 `.env` 檔）。

**使用 Maven：** 也可以從命令列運行：

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

展示零樣本、少樣本、思維鏈和角色扮演提示。

### 3. 函式呼叫

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI 會在需要時自動呼叫你的 Java 方法。

### 4. 文件問答 (簡易 RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

使用 Easy RAG 的自動嵌入和檢索功能，針對文件提出問題。

### 5. 負責任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

示範 AI 安全過濾機制如何屏蔽有害內容。

## 每個範例展示的內容

**基本聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

從這裡開始了解 LangChain4j 的基本用法。你會建立 `OpenAiOfficialChatModel`，用 `.chat()` 發送提示並取得回應。這展示了如何用自訂端點和 API 金鑰初始化模型。瞭解此模式後，其他功能都建立在這基礎上。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat:** 打開 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) 並問：
> - "我怎麼在這段程式碼中，從 GitHub 模型切換到 Azure OpenAI？"
> - "OpenAiOfficialChatModel.builder() 還能設定哪些參數？"
> - "如何新增串流回應，而不是等待完整回應？"

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

既然你知道如何與模型互動，我們看看你怎麼和模型說話。此範例使用相同模型設置，展示五種不同的提示範例。試試零樣本提示直接指令、少樣本提示從例子學習、思維鏈提示揭示推理步驟，以及角色扮演提示設定上下文。你會看到同一模型根據提示方式給出截然不同的結果。

範例也示範了提示模板（PromptTemplate），這是一種用變數建立可重用提示的強大方法。
以下示例使用 LangChain4j 的 `PromptTemplate` 填充變數。AI 將根據提供的目的地與活動回答。

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

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat:** 打開 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) 並問：
> - "零樣本和少樣本提示有什麼區別？什麼時候該用哪種？"
> - "溫度參數如何影響模型回應？"
> - "有什麼方法能防止生產環境中的提示注入攻擊？"
> - "如何為常見範例建立可重用的 PromptTemplate 物件？"

**工具整合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

這是 LangChain4j 強大的地方。你會用 `AiServices` 建立一個 AI 助手，可呼叫你的 Java 方法。只要用 `@Tool("說明")` 標註方法，LangChain4j 會自動管理 —— AI 根據使用者需求決定何時使用哪個工具。這示範了函式呼叫，是讓 AI 不只是答問，而能執行動作的關鍵技巧。

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

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat:** 打開 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) 並問：
> - "@Tool 標註是怎麼運作的？LangChain4j 背後怎麼處理？"
> - "AI 可以連續呼叫多個工具來解決複雜問題嗎？"
> - "如果工具丟出例外，該怎麼處理錯誤？"
> - "我如何整合真實 API，而不是用這個加減乘除範例？"

**文件問答 (簡易 RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

這裡你會看到使用 LangChain4j「簡易 RAG」的檢索增強生成 (RAG) 範例。文件會被載入、自動切分並嵌入到記憶體中，再由內容檢索器在查詢時提供相關片段給 AI。AI 是根據你的文件回答，而非一般知識。

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

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat:** 打開 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) 並問：
> - "RAG 如何幫助防止 AI 產生幻覺，相較只用模型訓練數據？"
> - "簡易方法和自訂 RAG 管線有什麼差別？"
> - "如果要擴展，處理多文件或更大知識庫，該怎麼做？"

**負責任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

建立多層防護的 AI 安全機制。此範例展示兩層保護協作：

**第一部分：LangChain4j 輸入護欄** — 在提示到達大型語言模型前阻擋危險提示。可建立客製化護欄檢查禁止字詞或模式，這在你的程式碼裡運行，快速且免費。

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

**第二部分：提供者安全過濾** — GitHub 模型內建過濾機制，彌補護欄可能漏掉的。你會看到嚴重違規時產生硬阻擋（HTTP 400 錯誤）和軟拒絕（AI 委婉拒絕）。

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) Chat:** 打開 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) 並問：
> - "什麼是 InputGuardrail？我怎麼建立自己的？"
> - "硬阻擋和軟拒絕有什麼差異？"
> - "為什麼要同時使用護欄和提供者過濾？"

## 後續步驟

**下一模組：** [01-introduction - 在 Azure 使用 LangChain4j 和 gpt-5 入門](../01-introduction/README.md)

---

**導覽：** [← 返回主頁](../README.md) | [下一章：模組 01 - 介紹 →](../01-introduction/README.md)

---

## 故障排除

### 首次 Maven 建構

**問題：** 初次 `mvn clean compile` 或 `mvn package` 需時較長（10-15 分鐘）

**原因：** Maven 首次建構時需要下載所有專案依賴（Spring Boot、LangChain4j 庫、Azure SDK 等）。

**解決方案：** 這是正常現象。之後建構速度會快很多，因為依賴已緩存於本機。下載時間取決於網路速度。

### PowerShell Maven 指令語法

**問題：** Maven 指令執行失敗，出現錯誤 `Unknown lifecycle phase ".mainClass=..."`
**原因**：PowerShell 將 `=` 解讀為變數指派運算子，導致 Maven 屬性語法錯誤

**解決方法**：在 Maven 命令前使用停止解析運算子 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 運算子告訴 PowerShell 將所有後續參數原樣傳送給 Maven，且不做解析。

### Windows PowerShell 表情符號顯示

**問題**：AI 回應在 PowerShell 中顯示亂碼（例如 `????` 或 `â??`）而非表情符號

**原因**：PowerShell 預設編碼不支援 UTF-8 表情符號

**解決方法**：執行 Java 應用程式前，先執行此命令：
```cmd
chcp 65001
```

這會強制終端機使用 UTF-8 編碼。或者使用 Windows Terminal，具有較佳的 Unicode 支援。

### 偵錯 API 呼叫

**問題**：AI 模型認證錯誤、速率限制或意外回應

**解決方法**：範例中包含 `.logRequests(true)` 和 `.logResponses(true)`，可在控制台顯示 API 呼叫內容，有助於排查認證錯誤、速率限制或異常回應。正式環境請移除這些標誌以減少日誌噪音。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件乃使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們力求準確，但請注意機器翻譯可能包含錯誤或不準確之處。原文應視為具權威性的資料來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生之任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->