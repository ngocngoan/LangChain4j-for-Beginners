# Module 00: 快速開始

## 目錄

- [簡介](../../../00-quick-start)
- [什麼是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依賴](../../../00-quick-start)
- [先決條件](../../../00-quick-start)
- [設定](../../../00-quick-start)
  - [1. 取得你的 GitHub 令牌](../../../00-quick-start)
  - [2. 設定你的令牌](../../../00-quick-start)
- [運行範例](../../../00-quick-start)
  - [1. 基本聊天](../../../00-quick-start)
  - [2. 提示範本](../../../00-quick-start)
  - [3. 函數呼叫](../../../00-quick-start)
  - [4. 文件問答 (RAG)](../../../00-quick-start)
  - [5. 負責任的 AI](../../../00-quick-start)
- [每個範例展示內容](../../../00-quick-start)
- [下一步](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 簡介

此快速開始旨在讓你盡快開始使用 LangChain4j。它涵蓋了使用 LangChain4j 和 GitHub 模型構建 AI 應用程式的基本內容。在後續模組中，你將使用 Azure OpenAI 與 LangChain4j 建立更進階的應用程式。

## 什麼是 LangChain4j？

LangChain4j 是一個 Java 函式庫，可簡化構建 AI 驅動的應用程式。你不需要處理 HTTP 客戶端和 JSON 解析，而是使用乾淨的 Java API。

LangChain 的「鏈」指的是將多個元件串連起來——你可能將提示鏈接到模型再到解析器，或將多個 AI 呼叫鏈接起來，其中一個輸出成為下一個輸入。這個快速開始專注於基礎，然後再探索更複雜的鏈。

<img src="../../../translated_images/zh-HK/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中鏈接元件——構建塊連接以創建強大的 AI 工作流程*

我們將使用三個核心元件：

**ChatLanguageModel** — AI 模型互動的介面。呼叫 `model.chat("prompt")` 並獲得回應字串。我們使用 `OpenAiOfficialChatModel`，它可與 OpenAI 相容端點如 GitHub 模型合作。

**AiServices** — 建立型別安全的 AI 服務介面。定義方法，加上 `@Tool` 註解，LangChain4j 負責編排。AI 在需要時會自動呼叫你的 Java 方法。

**MessageWindowChatMemory** — 維持對話歷史。沒有它，每個請求都是獨立的。使用它，AI 會記住之前的訊息，並保持多輪對話的上下文。

<img src="../../../translated_images/zh-HK/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架構——核心元件協同運作，為你的 AI 應用程式提供動力*

## LangChain4j 依賴

此快速開始使用兩個 Maven 依賴，在 [`pom.xml`](../../../00-quick-start/pom.xml) 中：

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

`langchain4j-open-ai-official` 模組提供 `OpenAiOfficialChatModel` 類，連接至 OpenAI 兼容的 API。GitHub 模型使用相同 API 格式，因此不需要特殊適配器—只需將基底 URL 指向 `https://models.github.ai/inference`。

## 先決條件

**使用開發容器？** Java 和 Maven 已預裝。你只需要一個 GitHub 個人存取令牌。

**本地開發：**
- Java 21+、Maven 3.9+
- GitHub 個人存取令牌（見下方說明）

> **提示：** 本模組使用 GitHub 模型的 `gpt-4.1-nano`。請勿在程式碼中修改模型名稱——它已設定好與 GitHub 可用模型兼容。

## 設定

### 1. 取得你的 GitHub 令牌

1. 前往 [GitHub 設定 → 個人存取令牌](https://github.com/settings/personal-access-tokens)
2. 點擊「產生新令牌」
3. 設定描述名稱（例如「LangChain4j Demo」）
4. 設定過期時間（建議 7 天）
5. 在「帳號權限」中找到「Models」，設為「唯讀」
6. 點擊「產生令牌」
7. 複製並保存令牌——你以後看不到它

### 2. 設定你的令牌

**選項 1：使用 VS Code（推薦）**

如果你使用 VS Code，將你的令牌加入專案根目錄的 `.env` 檔案：

如果 `.env` 檔案不存在，請將 `.env.example` 複製為 `.env`，或在專案根目錄建立新 `.env` 檔案。

**範例 `.env` 檔案：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

然後你只需在檔案樹中右鍵任何範例檔（例如 `BasicChatDemo.java`），選擇 **「執行 Java」**，或使用「執行與除錯」面板中的啟動設定。

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

## 運行範例

**使用 VS Code：** 直接在檔案樹中右鍵任何範例檔，選擇 **「執行 Java」**，或使用「執行與除錯」面板中的啟動設定（請先在 `.env` 檔案加入令牌）。

**使用 Maven：** 你也可以用命令行執行：

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

展示零次示例、少次示例、思考鏈與角色提示。

### 3. 函數呼叫

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI 在需要時會自動呼叫你的 Java 方法。

### 4. 文件問答（RAG）

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

查看 AI 安全過濾器如何阻擋有害內容。

## 每個範例展示內容

**基本聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

從這裡開始，體驗 LangChain4j 的最基礎使用。你會建立一個 `OpenAiOfficialChatModel`，用 `.chat()` 傳送提示，接著獲得回應。這示範了基礎：如何使用自訂端點及 API 金鑰初始化模型。了解此模式後，後續一切都建立在此之上。

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看看：** 打開 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)，問：
> - 「如何將此程式改用 Azure OpenAI 替代 GitHub 模型？」
> - 「OpenAiOfficialChatModel.builder() 可以設定哪些其他參數？」
> - 「怎麼新增串流回應，而不是等待完整回應？」

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

現在你知道如何與模型交談，接著探索如何向它說話。此範例使用相同模型設定，但示範五種不同的提示範式。試試零次提示（直接指示）、少次提示（從示例學習）、思考鏈提示（揭示推理過程）、角色提示（設定上下文）。你會看到同一模型因提示方式不同而產生極大差異。

範例還展示了提示範本，一種強大方式，能使用變數製作可重用的提示。
以下示例展示如何使用 LangChain4j 的 `PromptTemplate` 填充變數。AI 將根據提供的目的地和活動來回答。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看看：** 打開 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)，問：
> - 「零次提示和少次提示有什麼不同，什麼時候該用哪一種？」
> - 「溫度參數如何影響模型回應？」
> - 「有哪些方法可防止生產環境中的提示注入攻擊？」
> - 「如何建立可重用的 PromptTemplate 物件給常見模式？」

**工具整合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

LangChain4j 的威力開始展現。你會使用 `AiServices` 建立 AI 助手，能呼叫你的 Java 方法。只需在方法加上 `@Tool("說明")` 註解，LangChain4j 會處理其餘部分——AI 自動根據使用者需求決定何時使用哪個工具。這展示了函數呼叫功能，是建立可執行動作 AI（不只是回答問題）的關鍵技巧。

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看看：** 打開 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，問：
> - 「@Tool 註解如何運作，LangChain4j 背後如何處理？」
> - 「AI 可以連續呼叫多個工具，解決複雜問題嗎？」
> - 「如果工具發生例外錯誤，該怎麼處理？」
> - 「如何整合真實 API，而不是這個計算器範例？」

**文件問答（RAG）** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

在這裡你會了解 RAG（檢索增強生成）的基礎。不是依賴模型訓練資料，而是從 [`document.txt`](../../../00-quick-start/document.txt) 載入內容，並放入提示中。AI 根據你的文件回答，而非一般知識。這是建立能使用自己資料的系統的第一步。

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **注意：** 這個簡單方法把整篇文件裝入提示。對於大型檔案（>10KB），會超出上下文限制。第 03 模組將介紹分塊和向量搜尋，適合生產 RAG 系統。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看看：** 打開 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)，問：
> - 「RAG 怎麼防止 AI 幻覺，與使用模型訓練資料有何不同？」
> - 「這種簡單方法與使用向量嵌入檢索差異在哪裡？」
> - 「如何擴展以處理多份文件或更大型的知識庫？」
> - 「有哪些最佳實務可結構提示，確保 AI 只用所給上下文？」

**負責任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

建置多層防護的 AI 安全。此範例展示兩層保護結合運作：

**第 1 部分：LangChain4j 輸入護欄** — 在到達大型語言模型前攔截危險提示。創建自訂護欄，檢查禁止關鍵字或樣板。這些在程式碼中執行，快速且免費。

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

**第 2 部分：服務商安全過濾器** — GitHub 模型具備內建過濾器，捕捉護欄可能漏掉的情況。你會看到嚴重違規時的硬阻擋（HTTP 400 錯誤）及 AI 禮貌拒絕的軟拒絕。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看看：** 打開 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)，問：
> - 「什麼是 InputGuardrail，如何自訂？」
> - 「硬阻擋與軟拒絕有什麼不同？」
> - 「為什麼要同時使用護欄和服務商過濾器？」

## 下一步

**下一模組：** [01-introduction - LangChain4j 與 Azure 上的 gpt-5 入門](../01-introduction/README.md)

---

**導航：** [← 返回主頁](../README.md) | [下一篇：Module 01 - 介紹 →](../01-introduction/README.md)

---

## 故障排除

### 初次 Maven 建置

**問題**：初次執行 `mvn clean compile` 或 `mvn package` 需要很長時間（10-15 分鐘）

**原因**：Maven 首次建置時需要下載所有專案依賴（Spring Boot、LangChain4j 函式庫、Azure SDK 等）。

**解決方案**：這是正常行為。之後建置會更快，因為依賴會快取在本機。下載時間取決於你的網絡速度。
### PowerShell Maven 指令語法

**問題**：Maven 指令出現錯誤 `Unknown lifecycle phase ".mainClass=..."`

**原因**：PowerShell 將 `=` 解讀為變數指派運算符，破壞了 Maven 屬性語法

**解決方案**：在 Maven 指令前使用停止解析運算符 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 運算符會告訴 PowerShell 將所有後續參數直接傳給 Maven，不做解讀。

### Windows PowerShell 表情符號顯示

**問題**：AI 回應中表情符號顯示為亂碼（例如 `????` 或 `â??`）

**原因**：PowerShell 預設編碼不支援 UTF-8 表情符號

**解決方案**：執行 Java 應用程式前先運行此指令：
```cmd
chcp 65001
```

這會在終端機強制使用 UTF-8 編碼。另外可以改用支援較佳 Unicode 的 Windows Terminal。

### 除錯 API 調用

**問題**：AI 模型出現驗證錯誤、速率限制或意外回應

**解決方案**：範例中包含 `.logRequests(true)` 與 `.logResponses(true)`，可在主控台顯示 API 調用。這有助於排查驗證錯誤、速率限制或異常回應。在正式環境中可移除這些標記以減少日誌雜訊。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我們致力於確保準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件之原文版本應被視為權威資料來源。對於關鍵資訊，建議採用專業人工翻譯。我們對因使用此翻譯所引起之任何誤解或誤釋概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->