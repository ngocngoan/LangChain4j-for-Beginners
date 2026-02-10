# 模組 01：LangChain4j 入門

## 目錄

- [你將學到的內容](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [了解核心問題](../../../01-introduction)
- [了解 Tokens](../../../01-introduction)
- [記憶是如何運作的](../../../01-introduction)
- [如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎設施](../../../01-introduction)
- [在本地運行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [下一步](../../../01-introduction)

## 你將學到的內容

如果你完成了快速開始，你已經看到如何發送提示並獲得回應。這是基礎，但真正的應用需要更多東西。本模組教你如何構建能記住上下文並保持狀態的會話式 AI ── 這也是一次性的展示和生產就緒應用之間的差異。

整個指南中，我們將使用 Azure OpenAI 的 GPT-5.2，因為它先進的推理能力讓不同模式的行為更加明顯。當你加入記憶時，你會清楚看到差異。這讓你更容易理解每個組件對你的應用帶來的價值。

你將構建一個示範兩種模式的應用程式：

**無狀態聊天** ── 每次請求都是獨立的。模型不知道之前的訊息。這也是你在快速開始中使用的模式。

**有狀態會話** ── 每次請求都包含對話歷史。模型跨多輪保持上下文。這是生產應用所必需的。

## 先決條件

- 擁有 Azure 訂閱並可使用 Azure OpenAI
- Java 21，Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的開發容器中已預裝 Java、Maven、Azure CLI 及 Azure Developer CLI (azd)。

> **注意：** 本模組使用 Azure OpenAI 上的 GPT-5.2。部署透過 `azd up` 自動配置，請勿修改程式碼中的模型名稱。

## 了解核心問題

語言模型是無狀態的。每次 API 調用都是獨立的。如果你輸入「我叫 John」，然後問「我叫什麼名字？」，模型並不知道你剛剛自我介紹。它將每個請求視為第一次對話。

這對簡單問答可以，但對於真實應用毫無用處。客服機器人需要記住你說過的話，個人助理需要上下文，任何多輪對話都需要記憶。

<img src="../../../translated_images/zh-MO/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="無狀態與有狀態對話" width="800"/>

*無狀態（獨立呼叫）與有狀態（具上下文感知）對話的差異*

## 了解 Tokens

在深入會話前，理解 tokens 很重要 ── 這是語言模型處理的基本文字單位：

<img src="../../../translated_images/zh-MO/token-explanation.c39760d8ec650181.webp" alt="Token 解釋" width="800"/>

*文字如何拆分成 tokens 的例子──「I love AI!」變成 4 個獨立處理單位*

Tokens 是 AI 模型衡量和處理文字的方式。字詞、標點符號，甚至空格都可能是 tokens。你的模型有最大可處理的 tokens 數量（GPT-5.2 為 400,000，包含最多 272,000 輸入 tokens 和 128,000 輸出 tokens）。理解 tokens 有助你管理對話長度與成本。

## 記憶是如何運作的

聊天記憶解決了無狀態問題，透過保持對話歷史來達成。在發送請求到模型之前，框架會預先加入相關的先前訊息。當你問「我叫什麼名字？」，系統實際上是把整個對話歷史發給模型，讓它知道你之前說過「我叫 John」。

LangChain4j 提供了自動處理此問題的記憶實作。你選擇要保留多少訊息，框架幫你管理上下文窗口。

<img src="../../../translated_images/zh-MO/memory-window.bbe67f597eadabb3.webp" alt="記憶窗口概念" width="800"/>

*MessageWindowChatMemory 維護最近訊息的滑動視窗，自動丟棄舊訊息*

## 如何使用 LangChain4j

本模組擴展快速入門，整合 Spring Boot 並加入對話記憶。組件如何組合如下：

**依賴項** ── 新增兩個 LangChain4j 函式庫：

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

**聊天模型** ── 配置 Azure OpenAI 作為 Spring Bean（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

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

Builder 會從 `azd up` 設定的環境變數讀取憑證。將 `baseUrl` 指向你的 Azure 端點，讓 OpenAI 用戶端可搭配 Azure OpenAI 使用。

**對話記憶** ── 使用 MessageWindowChatMemory 追蹤聊天歷史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

使用 `withMaxMessages(10)` 建立記憶，保留最近 10 則訊息。藉由 `UserMessage.from(text)` 與 `AiMessage.from(text)` 的型別包裝，加入使用者與 AI 訊息。用 `memory.messages()` 取回歷史並傳給模型。此服務依對話 ID 儲存各自的記憶實例，支援多用戶同時聊天。

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 開啟 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) 並問：
> - 「當視窗已滿時，MessageWindowChatMemory 如何決定丟棄哪些訊息？」
> - 「我能否使用資料庫實作自訂記憶存儲，而非記憶體儲存？」
> - 「我要如何添加摘要功能，以壓縮舊的對話歷史？」

無狀態聊天端點完全跳過記憶，直接用 `chatModel.chat(prompt)`，和快速啟動一樣。有狀態端點則把訊息加到記憶，取出歷史，並在每次請求中帶上上下文。模型配置相同，模式不同。

## 部署 Azure OpenAI 基礎設施

**Bash:**
```bash
cd 01-introduction
azd up  # 選擇訂閱及位置（建議使用 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 選擇訂閱和位置（建議使用 eastus2）
```

> **注意：** 若遇到逾時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），請直接重跑一次 `azd up`。Azure 資源可能仍在背景配置，重試會讓部署在資源達到終端狀態後完成。

此操作將：
1. 部署具備 GPT-5.2 及 text-embedding-3-small 模型的 Azure OpenAI 資源
2. 自動於專案根目錄產生帶憑證的 `.env` 檔案
3. 設定所有必需的環境變數

**部署有問題嗎？** 請參閱 [基礎設施 README](infra/README.md) 獲得詳細故障排除，包括子域名衝突、手動在 Azure 入口網站部署步驟與模型配置指南。

**驗證部署成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 命令會自動產生 `.env` 檔。日後若需更新，可以直接手動編輯 `.env`，或重新執行：
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```


## 在本地運行應用程式

**驗證部署：**

確認根目錄有包含 Azure 憑證的 `.env` 檔：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**選項 1：使用 Spring Boot Dashboard（推薦 VS Code 使用者）**

開發容器已內建 Spring Boot Dashboard 擴充功能，提供視覺化介面管理所有 Spring Boot 應用。可在 VS Code 左側的活動列找到（尋找 Spring Boot 圖標）。

在 Spring Boot Dashboard 中可以：
- 查看工作區內所有可用 Spring Boot 應用
- 一鍵啟動/停止應用
- 實時查看應用日誌
- 監控應用狀態

直接點擊「introduction」旁的播放按鈕開啟本模組，或同時啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有網頁應用（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 從根目錄
.\start-all.ps1
```

或只啟動本模組：

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

兩個腳本會自動從根目錄 `.env` 載入環境變數，若 JAR 尚未建置會自動編譯。

> **注意：** 若你想先手動建置所有模組，再啟動：
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```


在瀏覽器開啟 http://localhost:8080。

**停止應用：**

**Bash:**
```bash
./stop.sh  # 只有此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 此模組僅限
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```


## 使用應用程式

應用程式提供了並排的兩種聊天實作的網頁介面。

<img src="../../../translated_images/zh-MO/home-screen.121a03206ab910c0.webp" alt="應用首頁螢幕" width="800"/>

*儀表板顯示簡單聊天（無狀態）與會話聊天（有狀態）兩種選項*

### 無狀態聊天（左側面板）

先試試這個。先說「我叫 John」，接著馬上問「我叫什麼名字？」模型不會記得，因為每條訊息都是獨立的。這展示了基本語言模型整合的核心問題──沒有對話上下文。

<img src="../../../translated_images/zh-MO/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="無狀態聊天示範" width="800"/>

*AI 不記得你上一句話裡的名字*

### 有狀態聊天（右側面板）

接著試試這邊一樣的流程。先說「我叫 John」，然後問「我叫什麼名字？」這次它會記得。差異在於 MessageWindowChatMemory──它維護會話歷史並在每次請求中包含這些內容。這就是生產會話式 AI 的運作方式。

<img src="../../../translated_images/zh-MO/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="有狀態聊天示範" width="800"/>

*AI 記得對話中較早時候你說的名字*

兩個面板使用相同 GPT-5.2 模型，唯一差別是記憶。這清楚說明了記憶對你的應用帶來的價值，以及為什麼對真實案例至關重要。

## 下一步

**下一模組：** [02-prompt-engineering - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導航：** [← 上一章節：模組 00 - 快速開始](../00-quick-start/README.md) | [回主頁](../README.md) | [下一章節：模組 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件使用人工智能翻譯服務【Co-op Translator】（https://github.com/Azure/co-op-translator）進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤譯負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->