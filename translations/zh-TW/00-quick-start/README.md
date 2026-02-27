# Module 00: 快速開始

## 目錄

- [介紹](../../../00-quick-start)
- [什麼是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依賴項](../../../00-quick-start)
- [先決條件](../../../00-quick-start)
- [設定](../../../00-quick-start)
  - [1. 取得你的 GitHub 令牌](../../../00-quick-start)
  - [2. 設定你的令牌](../../../00-quick-start)
- [執行範例](../../../00-quick-start)
  - [1. 基本聊天](../../../00-quick-start)
  - [2. 提示範本](../../../00-quick-start)
  - [3. 函數調用](../../../00-quick-start)
  - [4. 文件問答（簡易 RAG）](../../../00-quick-start)
  - [5. 負責任的 AI](../../../00-quick-start)
- [每個範例展示的內容](../../../00-quick-start)
- [下一步](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 介紹

此快速開始旨在讓你盡快開始使用 LangChain4j。涵蓋了使用 LangChain4j 和 GitHub 模型構建 AI 應用的絕對基礎。接下來的模組中，你將使用 Azure OpenAI 與 LangChain4j 建立更進階的應用。

## 什麼是 LangChain4j？

LangChain4j 是一個簡化構建 AI 應用的 Java 函式庫。你無需處理 HTTP 用戶端及 JSON 解析，而是使用簡潔明瞭的 Java API。

LangChain 中的「鏈」是指將多個組件串聯起來——例如將提示連接到模型，再連接到解析器，或將多個 AI 呼叫串聯在一起，一個輸出成為下一個輸入。本快速開始專注於基礎概念，之後會探索更複雜的鏈式組合。

<img src="../../../translated_images/zh-TW/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中的鏈式組件——積木式連接打造強大的 AI 工作流程*

我們將使用三個核心組件：

**ChatModel** - AI 模型交互的介面。呼叫 `model.chat("prompt")` 並獲得回應字串。我們使用 `OpenAiOfficialChatModel`，它適用於與 OpenAI 相容的端點，如 GitHub 模型。

**AiServices** - 創建型別安全的 AI 服務介面。定義方法並以 `@Tool` 註釋，LangChain4j 負責協調調用。AI 會在需要時自動呼叫你的 Java 方法。

**MessageWindowChatMemory** - 維護對話歷史。沒有它，每個請求都是獨立的；有了它，AI 會記住先前訊息，在多輪對話中保持上下文。

<img src="../../../translated_images/zh-TW/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架構——核心組件協同工作，驅動你的 AI 應用*

## LangChain4j 依賴項

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

`langchain4j-open-ai-official` 模組提供 `OpenAiOfficialChatModel` 類，可連接 OpenAI 相容的 API。GitHub 模型使用相同的 API 格式，無需特殊適配器，只需將基底 URL 指向 `https://models.github.ai/inference`。

`langchain4j-easy-rag` 模組提供自動文件切分、嵌入和檢索功能，讓你能輕鬆建立 RAG 應用，無需手動配置每一步。

## 先決條件

**使用開發容器？** Java 與 Maven 已安裝，只需一個 GitHub 個人存取令牌。

**本機開發：**
- Java 21+，Maven 3.9+
- GitHub 個人存取令牌（以下有說明）

> **注意：** 本模組使用 GitHub 模型的 `gpt-4.1-nano`。請勿修改程式碼中的模型名稱——它已配置為對應 GitHub 可用模型。

## 設定

### 1. 取得你的 GitHub 令牌

1. 前往 [GitHub 設定 → 個人存取令牌](https://github.com/settings/personal-access-tokens)
2. 點擊「產生新令牌」
3. 設定描述名稱（例如「LangChain4j Demo」）
4. 設定過期時間（建議 7 天）
5. 在「帳戶權限」中找到「Models」，設定為「唯讀」
6. 點擊「產生令牌」
7. 複製並保存你的令牌——之後無法再次查看

### 2. 設定你的令牌

**選項 1：使用 VS Code（建議）**

若使用 VS Code，將令牌新增至專案根目錄的 `.env` 檔案：

若無 `.env` 檔，請複製 `.env.example` 為 `.env`，或於專案根目錄新增 `.env`。

**範例 `.env` 檔案：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

之後在 Explorer 中右鍵任一示範檔（例如 `BasicChatDemo.java`），選擇 **「Run Java」**，或從「執行及除錯」面板使用啟動配置執行。

**選項 2：使用終端機**

設定令牌為環境變數：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 執行範例

**使用 VS Code：** 直接在 Explorer 中右鍵任一示範檔並選擇 **「Run Java」**，或從「執行及除錯」面板使用啟動配置（確保已先將令牌添加至 `.env` 檔）。

**使用 Maven：** 也可從命令列執行：

### 1. 基本聊天

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. 提示範本

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

展示零次學習、少次學習、思維鏈及角色基礎提示範本。

### 3. 函數調用

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

透過 Easy RAG 以自動嵌入和檢索向文件發問。

### 5. 負責任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

展示 AI 安全過濾器如何攔截有害內容。

## 每個範例展示的內容

**基本聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

從這裡開始，看見 LangChain4j 最簡單的面貌。你會建立一個 `OpenAiOfficialChatModel`，使用 `.chat()` 發送提示，並獲取回應。這展示了核心基礎：如何使用自訂端點與 API 金鑰初始化模型。理解此模式後，其他功能便建立在此之上。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 打開 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) 並提問：
> - 「如何將此程式碼中的 GitHub 模型切換到 Azure OpenAI？」
> - 「OpenAiOfficialChatModel.builder() 還可設定哪些參數？」
> - 「我怎麼加入串流回應，而不是等待完整回應？」

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

既然你知道如何與模型溝通，接下來探索你該對模型說什麼。本示範使用相同模型設定，但展示五種不同提示範本。試試零次學習直接指令、少次學習範例教學、思維鏈展現推理步驟，以及角色基礎設定上下文。你將看到同一模型根據提示框架，可以給出截然不同的結果。

示範也展現提示範板，它是創建帶變數可重用提示的強大方式。
以下範例展示使用 LangChain4j `PromptTemplate` 來填充變數。AI 將根據提供的目的地和活動回答。

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

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 打開 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) 並提問：
> - 「零次學習和少次學習提示有何差異，何時該使用？」
> - 「溫度參數會如何影響模型回應？」
> - 「防止提示注入攻擊有哪些技巧？」
> - 「如何為常見模式建立可重用的 PromptTemplate 物件？」

**工具整合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

這裡是 LangChain4j 強大的地方。你會使用 `AiServices` 創建 AI 助手，可呼叫你的 Java 方法。只要用 `@Tool("描述")` 註釋方法，LangChain4j 負責其餘——AI 根據用戶詢問自動決定何時使用哪個工具。此示範展示函數調用，建構能採取行動而不僅回答問題的 AI 關鍵技術。

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

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 打開 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) 並提問：
> - 「@Tool 註釋如何運作，LangChain4j 在背後做了什麼？」
> - 「AI 能否連續呼叫多個工具解決複雜問題？」
> - 「如果工具拋出例外，該如何處理錯誤？」
> - 「如何整合真實 API，而非這個計算器範例？」

**文件問答（簡易 RAG）** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

這裡展示使用 LangChain4j 的「Easy RAG」方法實現 RAG（檢索增強生成）。文件會被載入，自動分割並嵌入內存儲存，再由內容檢索器在查詢時提供相關片段給 AI。AI 根據你的文件回應，而非自身一般知識。

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

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 打開 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) 並提問：
> - 「相較於使用模型訓練數據，RAG 如何防止 AI 幻覺？」
> - 「這種簡易方法與自訂 RAG 管線的差異是什麼？」
> - 「我如何擴展至多文件或更大型知識庫？」

**負責任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

構建深度防護的 AI 安全性。此示範展示兩層保護協同作用：

**第一部分：LangChain4j 輸入防護措施** —— 在提示送入大型語言模型前阻擋危險訊息。建立自訂防護規則，檢查禁止關鍵字或模式。這些在你的程式碼中運行，速度快且免費。

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

**第二部分：提供者安全過濾器** —— GitHub 模型內建過濾器，攔截防護可能漏網的內容。你會見到嚴重違規的硬性封鎖（HTTP 400 錯誤）以及委婉拒絕的軟性封鎖。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 打開 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) 並提問：
> - 「什麼是 InputGuardrail？我該如何自訂？」
> - 「硬性封鎖與軟性拒絕的差別？」
> - 「為什麼同時使用防護措施和提供者過濾？」

## 下一步

**下一模組：** [01-introduction - 在 Azure 上使用 LangChain4j 與 gpt-5 開始](../01-introduction/README.md)

---

**導覽：** [← 返回主頁](../README.md) | [下一個：Module 01 - 介紹 →](../01-introduction/README.md)

---

## 故障排除

### 第一次 Maven 建置

**問題：** 初次執行 `mvn clean compile` 或 `mvn package` 時耗時很久（10-15 分鐘）

**原因：** Maven 需要在第一次建置時下載所有專案依賴（Spring Boot、LangChain4j 函式庫、Azure SDK 等）。

**解決方式：** 這是正常現象。後續建置將因本機快取而更快。下載時間視你的網路速度而定。

### PowerShell 執行 Maven 命令語法

**問題：** Maven 命令失敗，顯示錯誤 `Unknown lifecycle phase ".mainClass=..."`
**原因**：PowerShell 將 `=` 解讀為變數賦值運算子，導致 Maven 屬性語法中斷

**解決方案**：在 Maven 命令前使用停止解析運算子 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 運算子告訴 PowerShell 把剩下的參數全部原樣傳遞給 Maven，不做解析。

### Windows PowerShell Emoji 顯示問題

**問題**：AI 回應在 PowerShell 中顯示亂碼（例如 `????` 或 `â??`）而非表情符號

**原因**：PowerShell 預設編碼不支援 UTF-8 的表情符號

**解決方案**：在執行 Java 應用程式前執行此命令：
```cmd
chcp 65001
```

此命令強制終端機使用 UTF-8 編碼。或者，使用 Windows Terminal 會有較好的 Unicode 支援。

### 除錯 API 呼叫

**問題**：AI 模型產生認證錯誤、速率限制或非預期回應

**解決方案**：範例中包含 `.logRequests(true)` 和 `.logResponses(true)`，能在主控台顯示 API 呼叫，有助於偵錯認證錯誤、速率限制或非預期回應。生產環境請移除這些標誌以減少紀錄噪音。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用人工智慧翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於翻譯的準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯服務。因使用本翻譯所產生的任何誤解或誤釋，我們概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->