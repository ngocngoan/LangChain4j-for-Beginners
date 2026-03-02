# Module 00: 快速開始

## 目錄

- [介紹](../../../00-quick-start)
- [什麼是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依賴項](../../../00-quick-start)
- [先決條件](../../../00-quick-start)
- [設定](../../../00-quick-start)
  - [1. 取得你的 GitHub 代幣](../../../00-quick-start)
  - [2. 設定你的代幣](../../../00-quick-start)
- [執行範例](../../../00-quick-start)
  - [1. 基本對話](../../../00-quick-start)
  - [2. 提示模式](../../../00-quick-start)
  - [3. 函數呼叫](../../../00-quick-start)
  - [4. 文件問答（簡易 RAG）](../../../00-quick-start)
  - [5. 負責任的 AI](../../../00-quick-start)
- [每個範例展示的內容](../../../00-quick-start)
- [下一步](../../../00-quick-start)
- [疑難排解](../../../00-quick-start)

## 介紹

本快速開始旨在讓你盡快開始使用 LangChain4j。它涵蓋了使用 LangChain4j 與 GitHub 模型構建 AI 應用的基本知識。在接下來的模組中，你將轉用 Azure OpenAI 和 GPT-5.2，並深入探討每個概念。

## 什麼是 LangChain4j？

LangChain4j 是一個 Java 函式庫，簡化了使用 AI 技術的應用程式建構。你不需處理 HTTP 用戶端和 JSON 解析，而是使用清晰的 Java API。

LangChain 中的「鏈」指的是將多個組件串接在一起——你可能將一個提示串接到模型，再串接到解析器，或將多個 AI 呼叫串接在一起，其中一個輸出作為下一個輸入。本快速開始專注於基礎原理，之後再探討更複雜的鏈結。

<img src="../../../translated_images/zh-TW/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中的串接組件—構建塊連接以創造強大的 AI 工作流程*

我們將使用三個核心組件：

**ChatModel** - AI 模型互動的介面。呼叫 `model.chat("prompt")` 並取得回應字串。我們使用 `OpenAiOfficialChatModel`，它可用於與兼容 OpenAI 的端點（例如 GitHub 模型）配合使用。

**AiServices** - 創建類型安全的 AI 服務介面。定義方法，使用 `@Tool` 註解，LangChain4j 負責協調。當需要時，AI 會自動呼叫你的 Java 方法。

**MessageWindowChatMemory** - 維護對話歷史。沒有它時，每個請求是獨立的。使用它時，AI 會記住之前的訊息並在多輪對話中維持上下文。

<img src="../../../translated_images/zh-TW/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架構—核心組件協同運作以為你的 AI 應用提供動力*

## LangChain4j 依賴項

本快速開始在 [`pom.xml`](../../../00-quick-start/pom.xml) 中使用三個 Maven 依賴項：

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

`langchain4j-open-ai-official` 模組提供了連接至 OpenAI 兼容 API 的 `OpenAiOfficialChatModel` 類別。GitHub 模型使用相同的 API 格式，因此不需要特殊適配器，只要將基底 URL 指向 `https://models.github.ai/inference` 即可。

`langchain4j-easy-rag` 模組提供自動文件分割、嵌入和檢索功能，讓你能建立 RAG 應用，而無需手動配置每一步。

## 先決條件

**使用開發容器？** Java 和 Maven 已預裝。你只需要 GitHub 個人存取權杖。

**本地開發：**
- Java 21+，Maven 3.9+
- GitHub 個人存取權杖（以下有說明）

> **注意：** 本模組使用 GitHub 模型的 `gpt-4.1-nano`。請勿更改程式碼中的模型名稱——它經過配置以配合 GitHub 可用的模型。

## 設定

### 1. 取得你的 GitHub 代幣

1. 前往 [GitHub 設定 → 個人存取權杖](https://github.com/settings/personal-access-tokens)
2. 點擊「Generate new token」
3. 設定描述性名稱（例如 "LangChain4j Demo"）
4. 設定有效期限（建議 7 天）
5. 在「帳戶權限」中找到「Models」並設為「唯讀」
6. 點擊「Generate token」
7. 複製並保存你的代幣—此後將無法再看到

### 2. 設定你的代幣

**選項 1：使用 VS Code（推薦）**

如果使用 VS Code，將代幣新增到專案根目錄的 `.env` 檔案：

若 `.env` 檔案不存在，可複製 `.env.example` 為 `.env`，或在專案根目錄自行建立 `.env` 檔案。

**範例 `.env` 檔案：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

之後你只需在檔案總管中右鍵點擊任何範例檔（如 `BasicChatDemo.java`），選擇 **「Run Java」**，或者從「執行與除錯」面板使用啟動配置。

**選項 2：使用終端機**

將代幣設定成環境變數：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 執行範例

**使用 VS Code：** 在檔案總管中右鍵點擊任何範例檔，選擇 **「Run Java」**，或從「執行與除錯」面板使用啟動配置（請先將代幣加入 `.env` 檔案）。

**使用 Maven：** 也可從命令列執行：

### 1. 基本對話

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

展示零次示範、少次示範、思維連鎖和角色基礎的提示模式。

### 3. 函數呼叫

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI 會在需要時自動呼叫你的 Java 方法。

### 4. 文件問答（簡易 RAG）

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

使用簡易 RAG 透過自動嵌入和檢索，對文件提出問題。

### 5. 負責任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

查看 AI 安全過濾器如何阻擋有害內容。

## 每個範例展示的內容

**基本對話** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

從這裡開始了解最簡單的 LangChain4j。你將建立一個 `OpenAiOfficialChatModel`，使用 `.chat()` 傳送提示並取得回應。這展示了基礎：如何使用自訂端點和 API 金鑰初始化模型。理解這個模式後，其它依此展開。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) 並問：
> - 「我如何在這段程式碼中從 GitHub 模型切換到 Azure OpenAI？」
> - 「OpenAiOfficialChatModel.builder() 還可以設定哪些參數？」
> - 「如何加入串流回應，而不必等完整回應？」

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

現在你已知道如何和模型對話，接著探索應該對它說什麼。此範例使用相同模型設置，但展示五種不同的提示模式。嘗試零次示範直接指令、少次示範學習範例、思維連鎖揭示推理過程，與角色基礎提示設定上下文。你會看到同一模型根據請求的框架，結果截然不同。

範例也展示提示模板，這是建立可重用帶變數提示的有力方式。以下範例使用 LangChain4j 的 `PromptTemplate` 填入變數。AI 將根據提供的目的地與活動回答問題。

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

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) 並問：
> - 「零次示範和少次示範提示有何差異，何時該使用哪個？」
> - 「溫度參數如何影響模型回應？」
> - 「有哪些技術可防止生產環境中的提示注入攻擊？」
> - 「如何建立常用模式的 reusable PromptTemplate 物件？」

**工具整合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

這是 LangChain4j 強大的地方。你將使用 `AiServices` 創建能呼叫你 Java 方法的 AI 助手。只需在方法上註解 `@Tool("描述")`，LangChain4j 會處理其餘工作——AI 會根據用戶請求自動決定何時使用各工具。這示範函數呼叫，為建構能執行動作而不只是回答問題的 AI 關鍵技術。

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

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) 並問：
> - 「@Tool 註解如何運作，LangChain4j 背後如何處理它？」
> - 「AI 可以連續呼叫多個工具來解決複雜問題嗎？」
> - 「如果工具拋出例外，該如何處理錯誤？」
> - 「如何整合真實 API，而非此計算機範例？」

**文件問答（簡易 RAG）** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

這裡你會看到使用 LangChain4j 「簡易 RAG」的 RAG（檢索增強生成）。文件會被載入、自動拆分並嵌入到記憶體中，然後內容檢索器在查詢時供應相關段落給 AI。AI 根據你的文件回答，而非依賴其一般知識。

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

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) 並問：
> - 「RAG 如何防止 AI 幻覺，相較於使用模型訓練資料？」
> - 「這種簡易方法與自訂 RAG 流程有何差異？」
> - 「如何擴展以支援多份文件或更大知識庫？」

**負責任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

建立深度防禦的 AI 安全。此範例展示兩層保護同時運作：

**第一部分：LangChain4j 輸入防護措施** — 在提示到達 LLM 前阻擋危險內容。建立自訂防護措施檢查禁止關鍵字或模式。此部分在程式碼中執行，速度快且免費。

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

**第二部分：供應商安全過濾器** — GitHub 模型有內建過濾機制，攔截你防護措施可能遺漏的內容。你會看到嚴重違規時的硬封鎖（HTTP 400 錯誤）以及軟拒絕，AI 客氣地拒絕回答。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) 並問：
> - 「什麼是 InputGuardrail？如何建立自己的？」
> - 「硬封鎖和軟拒絕有何不同？」
> - 「為何要同時使用防護措施和供應商過濾器？」

## 下一步

**下一模組：** [01-introduction - LangChain4j 入門](../01-introduction/README.md)

---

**導覽：** [← 返回主頁](../README.md) | [下一頁：模組 01 - 介紹 →](../01-introduction/README.md)

---

## 疑難排解

### 第一次 Maven 建置

**問題**：首次執行 `mvn clean compile` 或 `mvn package` 需時較長（10-15 分鐘）

**原因**：Maven 需要首次下載所有專案依賴（Spring Boot、LangChain4j 函式庫、Azure SDK 等）

**解決方案**：此為正常行為。之後重複建置會快許多，因為依賴已快取至本地。下載時間取決於你的網路速度。

### PowerShell Maven 指令語法

**問題**：Maven 指令執行錯誤，顯示 `Unknown lifecycle phase ".mainClass=..."`
**原因**：PowerShell 將 `=` 解釋為變數賦值運算子，破壞了 Maven 屬性語法

**解決方案**：在 Maven 命令前使用停止解析運算子 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 運算子告訴 PowerShell 將所有剩餘的參數原樣傳遞給 Maven，不加以解析。

### Windows PowerShell 表情符號顯示

**問題**：AI 回應在 PowerShell 中顯示亂碼（例如 `????` 或 `â??`）而非表情符號

**原因**：PowerShell 預設編碼不支援 UTF-8 表情符號

**解決方案**：在執行 Java 應用程式前運行此命令：
```cmd
chcp 65001
```

這會強制終端機使用 UTF-8 編碼。或者，改用對 Unicode 支援更佳的 Windows Terminal。

### 除錯 API 呼叫

**問題**：AI 模型出現認證錯誤、速率限制或非預期回應

**解決方案**：範例程式碼包含 `.logRequests(true)` 和 `.logResponses(true)`，會在主控台顯示 API 呼叫，有助於排查認證錯誤、速率限制或非預期回應。上線時移除這些設定以減少日誌雜訊。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保翻譯的準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件以其母語版本為權威依據。對於關鍵資訊，建議採用專業人工翻譯。本公司不對因使用本翻譯所產生的任何誤解或錯誤詮釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->