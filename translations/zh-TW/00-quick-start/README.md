# Module 00: 快速開始

## 目錄

- [介紹](../../../00-quick-start)
- [什麼是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依賴項](../../../00-quick-start)
- [先決條件](../../../00-quick-start)
- [設置](../../../00-quick-start)
  - [1. 取得你的 GitHub 代幣](../../../00-quick-start)
  - [2. 設定你的代幣](../../../00-quick-start)
- [運行範例](../../../00-quick-start)
  - [1. 基本聊天](../../../00-quick-start)
  - [2. 提示模式](../../../00-quick-start)
  - [3. 函數調用](../../../00-quick-start)
  - [4. 文件問答 (RAG)](../../../00-quick-start)
  - [5. 負責任的 AI](../../../00-quick-start)
- [每個範例展示什麼](../../../00-quick-start)
- [後續步驟](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 介紹

這個快速開始旨在讓你盡快開始使用 LangChain4j。它涵蓋了使用 LangChain4j 和 GitHub Models 構建 AI 應用程式的絕對基礎。在接下來的模塊中，你將使用 Azure OpenAI 和 LangChain4j 來構建更高級的應用程式。

## 什麼是 LangChain4j？

LangChain4j 是一個簡化構建 AI 驅動應用程式的 Java 庫。你不需要處理 HTTP 客戶端和 JSON 解析，只需要使用乾淨的 Java API。

LangChain 中的「鏈」指的是將多個組件串接起來 —— 你可能將提示鏈接到模型，再鏈接到解析器，或者將多個 AI 調用串接在一起，一個輸出成為下一個輸入。這個快速開始聚焦於基礎，之後會探索更複雜的鏈。

<img src="../../../translated_images/zh-TW/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中的組件鏈接 — 構建區塊連接，創造強大的 AI 工作流程*

我們會使用三個核心組件：

**ChatLanguageModel** - AI 模型交互介面。調用 `model.chat("prompt")` 並獲得回應字串。我們使用 `OpenAiOfficialChatModel` ，它適用於 OpenAI 兼容的端點，如 GitHub Models。

**AiServices** - 創建類型安全的 AI 服務介面。定義方法，使用 `@Tool` 註解，LangChain4j 會處理中介流程。AI 會在需要時自動調用你的 Java 方法。

**MessageWindowChatMemory** - 維持對話歷史。沒有它，每次請求都是獨立的。有了它，AI 能記住之前的訊息並在多回合中保持上下文。

<img src="../../../translated_images/zh-TW/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架構 — 核心組件協同工作，為你的 AI 應用提供動力*

## LangChain4j 依賴項

本快速開始示範使用兩個 Maven 依賴，在 [`pom.xml`](../../../00-quick-start/pom.xml) 中：

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
```

`langchain4j-open-ai-official` 模組提供了連接 OpenAI 兼容 API 的 `OpenAiOfficialChatModel` 類。GitHub Models 使用相同的 API 格式，因此無需特殊適配器 — 只需將基礎 URL 指向 `https://models.github.ai/inference` 即可。

## 先決條件

**使用開發容器？** 已預裝 Java 和 Maven，你只需要一個 GitHub 個人訪問令牌。

**本地開發：**
- Java 21 以上，Maven 3.9 以上
- GitHub 個人訪問令牌（以下有說明）

> **注意：** 本模塊使用 GitHub Models 的 `gpt-4.1-nano`。不要修改程式碼中的模型名稱 — 它配置為與 GitHub 可用模型相容。

## 設置

### 1. 取得你的 GitHub 代幣

1. 前往 [GitHub 設定 → 個人訪問令牌](https://github.com/settings/personal-access-tokens)
2. 點擊「Generate new token」
3. 設定描述名稱（例如「LangChain4j Demo」）
4. 設定過期時間（建議 7 天）
5. 在「帳戶權限」中找到「Models」，設為「只讀」
6. 點擊「Generate token」
7. 複製並保存你的代幣 — 之後無法再次看到

### 2. 設定你的代幣

**選項 1：使用 VS Code（推薦）**

如果你使用 VS Code，將代幣加入專案根目錄的 `.env` 檔案：

若 `.env` 不存在，複製 `.env.example` 到 `.env`，或者自行在專案根目錄新增 `.env`。

**範例 `.env` 檔案：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env 中
GITHUB_TOKEN=your_token_here
```

然後，你可以在檔案總管中右鍵任何示範檔案（如 `BasicChatDemo.java`）並選擇 **「Run Java」**，或使用執行與除錯面板的啟動設定。

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

## 運行範例

**使用 VS Code：** 只需在檔案總管中右鍵任何示範檔案，選擇 **「Run Java」**，或使用執行與除錯面板的啟動設定（請先將代幣加入 `.env`）。

**使用 Maven：** 或者，你也可以從命令行運行：

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

展示零次提示、少次提示、思維鏈提示及角色提示。

### 3. 函數調用

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI 會在需要時自動調用你的 Java 方法。

### 4. 文件問答 (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

可針對 `document.txt` 內容提問。

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

## 每個範例展示什麼

**基本聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

從這開始，看到 LangChain4j 的最簡單用法。你會建立一個 `OpenAiOfficialChatModel`，用 `.chat()` 傳送提示，並取得回應。這展示了如何用自訂端點和 API 金鑰初始化模型的基礎。理解這個模式後，其他功能就建立在它之上。

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) 並詢問：
> -「如何在這段程式碼中將 GitHub Models 換成 Azure OpenAI？」
> -「OpenAiOfficialChatModel.builder() 還可以配置哪些參數？」
> -「如何加入串流回應，而不是等待完整回應？」

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

現在你知道如何與模型對話，我們來探討你該說什麼。此示範使用相同的模型設定，但展示五種不同的提示模式。嘗試零次提示以直接指示、少次提示以透過範例學習、思維鏈提示以揭示推理過程，以及角色提示設定上下文。你會看到同一模型依提示方式產生大為不同的結果。

示範還展示提示模板，這是用變數建立可重用提示的強大方式。
以下範例使用 LangChain4j 的 `PromptTemplate` 填入變數。AI 會根據指定的目的地與活動回答。

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

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) 並詢問：
> -「零次提示和少次提示有何不同？各自適合什麼情境？」
> -「溫度參數如何影響模型的回應？」
> -「有哪些技術可防止生產中遭受提示注入攻擊？」
> -「如何建立可重用的 PromptTemplate 物件來應用常見模式？」

**工具整合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

LangChain4j 在這裡展現威力。你會使用 `AiServices` 創建可呼叫你 Java 方法的 AI 助手。只要用 `@Tool("描述")` 註解方法，LangChain4j 則負責其餘 —— AI 會根據使用者需求，自動判斷何時使用何工具。這展示了函數調用，一種能讓 AI 採取行動而不只是回答問題的關鍵技術。

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) 並詢問：
> -「@Tool 註解是怎麼運作的？LangChain4j 內部怎麼處理？」
> -「AI 可以連續呼叫多個工具來解決複雜問題嗎？」
> -「若工具拋出例外，應如何處理錯誤？」
> -「如何整合真實 API 而不是這個計算器範例？」

**文件問答 (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

這裡展示 RAG（檢索增強生成）的基礎。你不是依賴模型的訓練資料，而是載入 [`document.txt`](../../../00-quick-start/document.txt) 的內容並包含在提示中。AI 根據你的文件回答，而非其一般知識。這是朝向能運用你自有資料系統的第一步。

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **注意：** 此簡單方式將整個文件載入提示。對於大檔案（>10KB），你將超出上下文限制。模塊 03 涵蓋分塊與向量搜尋，用於生產級 RAG 系統。

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) 並詢問：
> -「RAG 如何防止 AI 幻覺，與直接使用模型訓練資料有何不同？」
> -「此簡單方法與使用向量嵌入做檢索有何差異？」
> -「如何擴展此方法以處理多個文件或較大知識庫？」
> -「結構提示的最佳實踐是什麼，以確保 AI 只使用提供的上下文？」

**負責任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

建立多層防禦的 AI 安全。此示範展示兩層防護共同運作：

**第一部分：LangChain4j 輸入護欄** - 在到達大型語言模型前阻擋危險提示。創建自訂護欄，檢查禁止關鍵字或模式。此機制在你程式碼中執行，速度快且免費。

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

**第二部分：提供者安全過濾器** - GitHub Models 具有內建過濾器，補足你的護欄未捕獲的內容。你會看到嚴重違規的強制阻擋（HTTP 400 錯誤）及 AI 禮貌拒絕的軟拒絕。

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) 並詢問：
> -「什麼是 InputGuardrail？我該如何自訂？」
> -「硬阻擋和軟拒絕的區別是什麼？」
> -「為什麼同時使用護欄和提供者過濾器？」

## 後續步驟

**下一模塊：** [01-introduction - 使用 LangChain4j 和 Azure 上的 gpt-5 入門](../01-introduction/README.md)

---

**導覽：** [← 返回主頁](../README.md) | [下一步：模塊 01 - 介紹 →](../01-introduction/README.md)

---

## 故障排除

### 初次 Maven 編譯

**問題：** 初次執行 `mvn clean compile` 或 `mvn package` 需要很長時間（10-15 分鐘）

**原因：** Maven 在第一次編譯時需要下載所有專案依賴（Spring Boot、LangChain4j 函式庫、Azure SDK 等）。

**解決方案：** 這是正常行為。後續編譯會快得多，因為依賴會在本地緩存。下載時間視你的網路速度而定。
### PowerShell Maven 命令語法

**問題**：Maven 命令失敗並顯示錯誤 `Unknown lifecycle phase ".mainClass=..."`

**原因**：PowerShell 將 `=` 解讀為變數賦值運算子，導致 Maven 屬性語法被破壞

**解決方案**：在 Maven 命令前使用停止解析運算子 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 運算子告訴 PowerShell 將後續所有參數字面傳遞給 Maven，不做解析。

### Windows PowerShell Emoji 顯示

**問題**：AI 回應在 PowerShell 中顯示亂碼（例如 `????` 或 `â??`）而非表情符號

**原因**：PowerShell 預設編碼不支援 UTF-8 表情符號

**解決方案**：在執行 Java 應用程式前執行此命令：
```cmd
chcp 65001
```

這會強制終端機使用 UTF-8 編碼。或者，可改用支援較好 Unicode 的 Windows Terminal。

### 除錯 API 呼叫

**問題**：來自 AI 模型的驗證錯誤、速率限制或意外回應

**解決方案**：範例中包含 `.logRequests(true)` 和 `.logResponses(true)` 用以在主控台顯示 API 呼叫。這有助於排除驗證錯誤、速率限制或意外回應。生產環境中請移除這些旗標以減少日誌雜訊。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保翻譯的準確性，但請注意，自動翻譯可能會包含錯誤或不準確之處。原始文件之原文版本應視為權威依據。對於關鍵資訊，建議尋求專業人工翻譯。我們不對因使用此翻譯而產生之任何誤解或誤譯負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->