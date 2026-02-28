# Module 01：開始使用 LangChain4j

## 目錄

- [影片導覽](../../../01-introduction)
- [你將學到的內容](../../../01-introduction)
- [前置條件](../../../01-introduction)
- [了解核心問題](../../../01-introduction)
- [理解 Tokens](../../../01-introduction)
- [記憶如何運作](../../../01-introduction)
- [本模組如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎架構](../../../01-introduction)
- [本地執行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [後續步驟](../../../01-introduction)

## 影片導覽

觀看這個實況課程，說明如何開始使用本模組：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 你將學到的內容

如果你完成了快速開始，你已經看到如何傳送提示詞並獲得回應。這是基礎，但真正的應用需要更多。本模組將教你如何打造能記憶上下文和維持狀態的對話式 AI —— 這是一次性展示和生產就緒應用之間的差異。

本指南將全程使用 Azure OpenAI 的 GPT-5.2，因為其先進的推理能力使不同模式的行為更明顯。當你加入記憶時，你將清楚看出差異，有助於理解各組件如何為你的應用帶來價值。

你將構建一個演示兩種對話模式的應用：

**無狀態聊天** —— 每次請求皆為獨立，模型不記得先前的訊息。這是你在快速開始中使用的模式。

**有狀態對話** —— 每次請求都帶有對話歷史，模型可跨多輪維持上下文。這是生產應用所需的。

## 前置條件

- 擁有 Azure 訂閱並具備 Azure OpenAI 存取權限
- Java 21，Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的開發容器中已預裝 Java、Maven、Azure CLI 及 Azure Developer CLI (azd)。

> **注意：** 本模組使用 Azure OpenAI 的 GPT-5.2。部署透過 `azd up` 會自動設定 —— 請勿在程式碼中修改模型名稱。

## 了解核心問題

語言模型是無狀態的。每次 API 呼叫彼此獨立。如果你傳送「我的名字是 John」然後問「我的名字是什麼？」，模型不會知道你剛剛自我介紹過。它把每次請求都當成你第一次對話。

這樣對於簡單的問答還算可以，但對真正的應用毫無用處。客服機器人需要記得你告訴它的內容。個人助理需要上下文。任何多輪對話都需要記憶。

<img src="../../../translated_images/zh-MO/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*無狀態（獨立呼叫）與有狀態（上下文感知）對話的差異*

## 理解 Tokens

在深入對話之前，理解 tokens 是重要的 —— tokens 是語言模型處理文本的基本單位：

<img src="../../../translated_images/zh-MO/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*範例展示文字如何拆分為 tokens —「我愛 AI！」變成四個獨立處理單位*

Tokens 是 AI 模型衡量及處理文字的方式。詞語、標點、甚至空格都可以是 tokens。你的模型對一次可處理的 token 數有上限（GPT-5.2 為 400,000，包含最多 272,000 輸入 tokens 和 128,000 輸出 tokens）。理解 tokens 有助於管理對話長度和成本。

## 記憶如何運作

聊天記憶解決無狀態問題，方式是維護對話歷史。在將請求傳給模型之前，框架會附加相關的先前訊息。當你問「我的名字是什麼？」時，系統實際上是傳送整個對話歷史，讓模型看到你之前說過「我的名字是 John」。

LangChain4j 提供的記憶實作會自動處理這部分。你只需選擇保留多少訊息，框架會管理整個上下文視窗。

<img src="../../../translated_images/zh-MO/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 維持最近訊息的滑動視窗，自動刪除較舊訊息*

## 本模組如何使用 LangChain4j

本模組基於快速開始擴充，整合 Spring Boot 並加入對話記憶。以下是各部分如何結合：

**相依套件** —— 新增兩個 LangChain4j 程式庫：

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

**聊天模型** —— 將 Azure OpenAI 設定為 Spring bean（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

建構器從 `azd up` 設定的環境變數讀取認證。設定 `baseUrl` 為你的 Azure 端點，讓 OpenAI 客戶端能與 Azure OpenAI 搭配使用。

**對話記憶** —— 使用 MessageWindowChatMemory 跟蹤聊天歷史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

以 `withMaxMessages(10)` 建立記憶限制保留最後 10 筆訊息。使用強型別包裝新增用戶及 AI 訊息：`UserMessage.from(text)` 和 `AiMessage.from(text)`。用 `memory.messages()` 取得歷史並送至模型。此服務會依對話 ID 分別儲存記憶實例，支援多用戶同時聊天。

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 開啟 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) 並詢問：
> - 「MessageWindowChatMemory 如何決定視窗滿時該丟棄哪些訊息？」
> - 「我可以用資料庫實作自訂記憶儲存，而非用記憶體嗎？」
> - 「我要怎麼加入摘要功能以壓縮舊的對話歷史？」

無狀態聊天端點完全不使用記憶 —— 就像快速開始中一樣使用 `chatModel.chat(prompt)`。有狀態端點將訊息加到記憶中，取出歷史並隨每次請求帶上上下文。模型設定相同，模式不同。

## 部署 Azure OpenAI 基礎架構

**Bash：**
```bash
cd 01-introduction
azd up  # 選擇訂閱和地點（建議使用 eastus2）
```

**PowerShell：**
```powershell
cd 01-introduction
azd up  # 選擇訂閱和位置（建議使用 eastus2）
```

> **注意：** 若遇到逾時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），只要再執行一次 `azd up`。Azure 資源可能仍在後台佈建，重新嘗試能等到資源進入終止狀態並完成部署。

這會：
1. 部署 Azure OpenAI 資源，包含 GPT-5.2 和 text-embedding-3-small 模型
2. 自動在專案根目錄產生 `.env` 憑證檔
3. 設定所有所需環境變數

**遇到部署問題？** 詳見 [基礎架構 README](infra/README.md) 以取得詳細故障排除，例如子域名稱衝突、手動 Azure 入口網站部署步驟和模型設定指引。

**確認部署成功：**

**Bash：**
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell：**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 會自動產生 `.env` 檔。若日後需更新，可以手動編輯 `.env`，或重新執行：
>
> **Bash：**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell：**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## 本地執行應用程式

**確認部署：**

確定在根目錄存在帶有 Azure 憑證的 `.env` 檔：

**Bash：**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell：**
```powershell
Get-Content ..\.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**選項 1：使用 Spring Boot Dashboard（推薦 VS Code 用戶）**

開發容器已內建 Spring Boot Dashboard 擴充，提供可視化介面管理所有 Spring Boot 應用。可在 VS Code 左側活動列找到（尋找 Spring Boot 圖示）。

你可以在 Spring Boot Dashboard：
- 查看工作區中所有可用的 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時查看應用日誌
- 監控應用狀態

只要點擊 “introduction” 旁的播放鍵啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有網頁應用（模組 01-04）：

**Bash：**
```bash
cd ..  # 從根目錄開始
./start-all.sh
```

**PowerShell：**
```powershell
cd ..  # 由根目錄開始
.\start-all.ps1
```

或只啟動本模組：

**Bash：**
```bash
cd 01-introduction
./start.sh
```

**PowerShell：**
```powershell
cd 01-introduction
.\start.ps1
```

兩腳本會自動從根目錄 `.env` 載入環境變數，且若 JAR 不存在會自動編譯。

> **注意：** 若你想先手動建置所有模組再啟動：
>
> **Bash：**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell：**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

於瀏覽器打開 http://localhost:8080 。

**停止應用：**

**Bash：**
```bash
./stop.sh  # 僅限此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell：**
```powershell
.\stop.ps1  # 僅此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

應用程式提供一個網頁介面，側邊並排兩種聊天實作。

<img src="../../../translated_images/zh-MO/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*主控台顯示簡易聊天（無狀態）和對話聊天（有狀態）選項*

### 無狀態聊天（左側面板）

先試這個。問「我的名字是 John」，接著立刻問「我的名字是什麼？」模型不會記得，因為每則訊息互不相關。這就是基本語言模型整合的核心問題 —— 無對話上下文。

<img src="../../../translated_images/zh-MO/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不記得你上一則訊息裡的名字*

### 有狀態聊天（右側面板）

現在同一序列在此嘗試。問「我的名字是 John」，再問「我的名字是什麼？」這次模型會記得。差異在 MessageWindowChatMemory —— 它維護對話歷史並隨請求帶上上下文。這是生產對話 AI 的運作方式。

<img src="../../../translated_images/zh-MO/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 記得你先前對話中的名字*

兩側面板均使用相同 GPT-5.2 模型。唯一差別是記憶。這清楚表明記憶為你的應用帶來了什麼，以及為何它對於真實用例至關重要。

## 後續步驟

**下一模組：** [02-prompt-engineering - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導航：** [← 上一章：Module 00 - 快速開始](../00-quick-start/README.md) | [返回首頁](../README.md) | [下一章：Module 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始語言文件應被視為權威來源。對於重要資訊，建議使用專業人工翻譯。我們對因使用本翻譯而引起的任何誤解或誤釋不承擔任何責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->