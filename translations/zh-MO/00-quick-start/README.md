# Module 00: 快速入門

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
  - [2. 提示模式](../../../00-quick-start)
  - [3. 函式呼叫](../../../00-quick-start)
  - [4. 文件問答 (RAG)](../../../00-quick-start)
  - [5. 負責任的 AI](../../../00-quick-start)
- [每個範例展示什麼](../../../00-quick-start)
- [後續步驟](../../../00-quick-start)
- [疑難排解](../../../00-quick-start)

## 介紹

此快速入門旨在讓您盡快開始使用 LangChain4j。內容涵蓋使用 LangChain4j 與 GitHub 模型構建 AI 應用程序的基本要素。在接下來的模組中，您將使用 Azure OpenAI 與 LangChain4j 建立更先進的應用。

## 什麼是 LangChain4j？

LangChain4j 是一個簡化構建 AI 應用的 Java 函式庫。您不需要處理 HTTP 客戶端和 JSON 解析，而是使用簡潔的 Java API。

LangChain 中的“chain”意指串接多個元件──您可能將提示串接到模型，再串接到解析器，或多次串接 AI 呼叫，一個輸出成為下一個的輸入。此快速入門聚焦基礎，稍後再探討更複雜的串接。

<img src="../../../translated_images/zh-MO/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中的串接元件──建構基石連結創建強大的 AI 工作流程*

我們將使用三個核心元件：

**ChatLanguageModel** - 與 AI 模型互動的介面。呼叫 `model.chat("prompt")` 並取得回應字串。我們使用與 OpenAI 兼容端點（例如 GitHub Models）配合的 `OpenAiOfficialChatModel`。

**AiServices** - 建立型別安全的 AI 服務介面。定義方法，使用 `@Tool` 標註，LangChain4j 自動協調。 AI 會在需要時自動呼叫您的 Java 方法。

**MessageWindowChatMemory** - 維護對話歷史。沒有它，每次請求是獨立的；有了它，AI 會記住先前訊息並維持多輪對話的上下文。

<img src="../../../translated_images/zh-MO/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架構──核心元件共同運作，為你的 AI 應用賦能*

## LangChain4j 依賴項

此快速入門在 [`pom.xml`](../../../00-quick-start/pom.xml) 中使用兩個 Maven 依賴：

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

`langchain4j-open-ai-official` 模組提供了連接 OpenAI 兼容 API 的 `OpenAiOfficialChatModel` 類別。GitHub Models 使用相同的 API 格式，無需特別適配器──只需將基本 URL 指向 `https://models.github.ai/inference`。

## 先決條件

**使用開發容器？** Java 和 Maven 已預先安裝，您只需要取得 GitHub 個人存取令牌。

**本機開發：**
- Java 21+，Maven 3.9+
- GitHub 個人存取令牌（以下說明）

> **注意：** 本模組使用來自 GitHub Models 的 `gpt-4.1-nano`，請勿修改程式碼中的模型名稱──它已配置為對應 GitHub 可用模型。

## 設定

### 1. 取得你的 GitHub 令牌

1. 前往 [GitHub 設定 → 個人存取令牌](https://github.com/settings/personal-access-tokens)
2. 點擊「產生新令牌」
3. 設定描述性名稱（例如 “LangChain4j Demo”）
4. 設定過期時間（建議 7 天）
5. 在「帳戶權限」中找到「Models」並設為「唯讀」
6. 點擊「產生令牌」
7. 複製並妥善保存你的令牌，之後無法再看到

### 2. 設定你的令牌

**選項 1：使用 VS Code（推薦）**

若您使用 VS Code，請將令牌新增到專案根目錄的 `.env` 檔案：

如果沒有 `.env` 檔，請將 `.env.example` 複製為 `.env`，或自行建立一個新的 `.env` 檔案。

**範例 `.env` 檔案：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

然後您可於檔案總管中右鍵點擊任何示範檔案（例如 `BasicChatDemo.java`），選擇 **「Run Java」**，或從執行與除錯面板使用啟動配置。

**選項 2：使用終端機**

將令牌設為環境變數：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 執行範例

**使用 VS Code：** 只需在檔案總管中右鍵點擊任一示範檔，選擇 **「Run Java」**，或使用執行與除錯面板的啟動配置（先確定您已將令牌加入 `.env` 檔）。

**使用 Maven：** 您也可以從命令行執行：

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

展示零次示例、少次示例、思路鏈以及角色式提示。

### 3. 函式呼叫

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI 會在需要時自動呼叫您的 Java 方法。

### 4. 文件問答 (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

針對 `document.txt` 中的內容提問。

### 5. 負責任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

展示 AI 安全過濾器如何阻擋有害內容。

## 每個範例展示什麼

**基本聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

從這裡開始，看看 LangChain4j 的最簡單示範。您將建立 `OpenAiOfficialChatModel`，使用 `.chat()` 傳送提示並獲得回應。這展示了基礎：如何用自訂端點和 API 金鑰初始化模型。熟悉此模式後，其他範例都建立在此基礎。

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 與 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) 並詢問：
> - 「我如何把這段程式從 GitHub Models 換成 Azure OpenAI？」
> - 「OpenAiOfficialChatModel.builder() 裡我還能設定什麼參數？」
> - 「如何新增串流回應，而不是等待整個回應完成？」

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

現在您知道如何與模型對話，接著探索您對它說什麼。此範例使用相同模型設定，但展現五種不同的提示模式。嘗試零次示例直接指令、少次示例學習範例、思路鏈揭露推理步驟，及設定上下文的角色式提示。您會看到相同模型如何根據提示方式產生截然不同的結果。

範例同時示範提示模板，這是創建可重用包含變量提示的強大方式。
以下示範用 LangChain4j 的 `PromptTemplate` 填入變數，AI 會基於提供的目的地和活動給出回答。

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

> **🤖 與 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) 並詢問：
> - 「零次示例和少次示例有什麼不同？什麼時候該用哪種？」
> - 「temperature 這個參數怎麼影響模型回應？」
> - 「有哪些技巧可防止生產環境中提示注入攻擊？」
> - 「我如何為常用模式建立可重用的 PromptTemplate 物件？」

**工具整合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

這是 LangChain4j 強大之處。您會用 `AiServices` 建立 AI 助手，能呼叫您的 Java 方法。只要用 `@Tool("描述")` 標註方法，LangChain4j 會負責其餘協調──AI 根據使用者請求自動決定何時調用。這示範了函式呼叫，是建構能執行操作 AI（不只是答題）的關鍵技巧。

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 與 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) 並詢問：
> - 「@Tool 標註是怎麼運作的？LangChain4j 背後怎麼處理？」
> - 「AI 可以連續呼叫多個工具來解決複雜問題嗎？」
> - 「如果工具拋出例外，我該怎麼處理錯誤？」
> - 「我要如何整合真實 API，替換這個計算器範例？」

**文件問答 (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

這裡是 RAG（檢索增強生成）的基礎。您不再仰賴模型的訓練資料，而是從 [`document.txt`](../../../00-quick-start/document.txt) 載入內容並包含於提示中。AI 依據您的文件回答，而非其一般知識。這是建構能使用自有資料系統的第一步。

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **注意：** 這是簡單方法，把整份文件加載進提示。若檔案過大（>10KB），會超出上下文限制。模組 03 講解如何做切片和向量搜尋來實作生產級 RAG 系統。

> **🤖 與 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) 並詢問：
> - 「相比用模型訓練資料，RAG 如何防止 AI 幻覺？」
> - 「這種簡單方法和用向量嵌入做檢索有何不同？」
> - 「我如何擴展以處理多份文件或更大的知識庫？」
> - 「怎麼設計提示以確保 AI 只用所提供的上下文？」

**負責任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

建立防禦深度的 AI 安全。此範例示範兩層防護共同工作：

**第一部分：LangChain4j 輸入護欄** - 在到達 LLM 前阻擋危險提示。自訂護欄檢查禁用關鍵字或模式。這在程式碼中執行，所以快速且免費。

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

**第二部分：供應商安全過濾器** - GitHub Models 內建過濾器，捕捉護欄可能漏網之魚。您會見到嚴重違規的硬封鎖（HTTP 400 錯誤）與 AI 禮貌拒絕的軟拒絕。

> **🤖 與 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) 並詢問：
> - 「什麼是 InputGuardrail？我怎麼建立自己的？」
> - 「硬封鎖和軟拒絕有什麼差異？」
> - 「為什麼要同時使用護欄和供應商過濾器？」

## 後續步驟

**下一模組：** [01-introduction - 使用 LangChain4j 與 Azure 上的 gpt-5 快速入門](../01-introduction/README.md)

---

**導覽：** [← 返回主頁](../README.md) | [下一步：模組 01 - 介紹 →](../01-introduction/README.md)

---

## 疑難排解

### 初次 Maven 建置

**問題：** 首次執行 `mvn clean compile` 或 `mvn package` 花費時間過久（10-15 分鐘）

**原因：** Maven 需在首次建置時下載所有專案相依（Spring Boot、LangChain4j 函式庫、Azure SDK 等）。

**解決方案：** 這是正常行為。後續建置會快速許多，因為相依已快取至本地。下載速度取決於您的網路環境。
### PowerShell Maven 指令語法

**問題**：Maven 指令出錯，顯示 `Unknown lifecycle phase ".mainClass=..."`

**原因**：PowerShell 將 `=` 解讀為變數賦值運算符，導致 Maven 屬性語法錯亂

**解決方法**：在 Maven 指令前使用停止解析運算符 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 運算符告訴 PowerShell 將所有後續參數字面傳遞給 Maven，不做解析。

### Windows PowerShell Emoji 顯示問題

**問題**：AI 回應在 PowerShell 顯示為亂碼（例如 `????` 或 `â??`），取代 emoji

**原因**：PowerShell 預設編碼不支援 UTF-8 emoji

**解決方法**：執行 Java 應用程式前運行此命令：
```cmd
chcp 65001
```

這會強制終端機使用 UTF-8 編碼。或者，使用支援較佳 Unicode 的 Windows Terminal。

### 調試 API 呼叫

**問題**：AI 模型出現身份驗證錯誤、速率限制或非預期回應

**解決方法**：範例中包含 `.logRequests(true)` 和 `.logResponses(true)`，可在主控台顯示 API 呼叫，協助排查認證錯誤、速率限制或非預期回應。生產環境請移除這些標誌以減少日誌雜訊。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用此翻譯而引起的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->