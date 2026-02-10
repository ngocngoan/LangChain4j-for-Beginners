# Module 01: 使用 LangChain4j 入門

## 目錄

- [你將學到什麼](../../../01-introduction)
- [先備條件](../../../01-introduction)
- [理解核心問題](../../../01-introduction)
- [了解 Tokens](../../../01-introduction)
- [記憶的運作方式](../../../01-introduction)
- [本範例如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎設施](../../../01-introduction)
- [在本地執行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [下一步](../../../01-introduction)

## 你將學到什麼

如果你完成了快速入門，就已經看到如何送出提示並獲得回應。這是基礎，但真實的應用程式需要更多功能。本單元教你如何建立能記憶上下文並維持狀態的對話式 AI — 這是一次性示範與生產級應用程式之間的差異。

在本指南中，我們將持續使用 Azure OpenAI 的 GPT-5.2，因為其進階推理能力能更清楚展現不同模式的行為差異。當你加入記憶功能，差異將變得非常明顯，讓你更容易理解每個元件為應用程式帶來的價值。

你將建置一個示範兩種模式的應用程式：

**無狀態聊天** - 每次請求相互獨立。模型不會記得先前的對話訊息。這是你在快速入門時使用的模式。

**有狀態對話** - 每次請求都包含對話歷史。模型能夠跨多回合維持上下文。這是生產應用程式所需的模式。

## 先備條件

- 具有 Azure OpenAI 存取權的 Azure 訂閱
- Java 21、Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure 開發者 CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** Java、Maven、Azure CLI 和 Azure 開發者 CLI (azd) 都已預先安裝在提供的開發容器中。

> **注意：** 本模組使用 Azure OpenAI 的 GPT-5.2。部署會透過 `azd up` 自動設定 — 請勿修改程式碼中的模型名稱。

## 理解核心問題

語言模型是無狀態的。每次 API 呼叫彼此獨立。如果你先說「My name is John」，接著問「What's my name?」，模型根本不知道你剛剛自我介紹過。它會把每次請求當成你人生中第一次對話。

這種方式在簡單問答中還算可以，但在實務應用完全沒用。客服機器人需要記得你說過的話。個人助理需要上下文。任何多回合對話都需要具備記憶功能。

<img src="../../../translated_images/zh-TW/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*無狀態（獨立呼叫）與有狀態（具上下文）對話的差異*

## 了解 Tokens

在深入對話之前，理解 tokens 很重要 — tokens 是語言模型處理文字的基本單位：

<img src="../../../translated_images/zh-TW/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*範例顯示文字如何拆成 tokens —「I love AI!」變成 4 個獨立處理單位*

Tokens 是 AI 模型計量與處理文字的方式。單字、標點符號甚至空格都可能是 token。你的模型有一次能處理的 token 數限制（GPT-5.2 為 400,000 個，輸入最多 272,000 個，輸出最多 128,000 個）。了解 token 有助於管理對話長度和成本。

## 記憶的運作方式

聊天記憶解決無狀態問題，即維持對話歷史。在送出請求之前，框架會將相關之前訊息加在前面。當你問「What's my name?」時，系統會真正送出整個對話歷史，讓模型知道你之前說過「My name is John」。

LangChain4j 提供自動處理此功能的記憶實作。你只需選擇保留多少訊息，框架負責管理上下文視窗。

<img src="../../../translated_images/zh-TW/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 維持一個最近訊息的滑動視窗，自動丟棄舊訊息*

## 本範例如何使用 LangChain4j

本模組擴充快速入門範例，整合 Spring Boot 並加入對話記憶。架構說明如下：

**依賴套件** — 新增兩個 LangChain4j 函式庫：

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

**聊天模型** — 將 Azure OpenAI 設定為 Spring bean（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

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

建構器會從 `azd up` 設定的環境變數讀取認證。`baseUrl` 指向你的 Azure 端點，讓 OpenAI 用戶端能用 Azure OpenAI。

**對話記憶** — 用 MessageWindowChatMemory 跟蹤聊天歷史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

用 `withMaxMessages(10)` 建立記憶，保留最近 10 則訊息。使用類型包裝加訊息：`UserMessage.from(text)` 和 `AiMessage.from(text)`。透過 `memory.messages()` 取得歷史並送給模型。服務依對話 ID 儲存獨立記憶區，讓多使用者能同時聊天。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 打開 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) 並問：
> - 「MessageWindowChatMemory 滿了時如何決定要丟棄哪則訊息？」
> - 「我能用資料庫實作自訂記憶儲存，而非使用記憶體嗎？」
> - 「怎麼加摘要功能，壓縮過往對話歷史？」

無狀態聊天端點完全跳過記憶 — 就像快速入門一樣用 `chatModel.chat(prompt)`。有狀態端點會加訊息到記憶，取得歷史並把上下文包含在每次請求。模型設定相同，模式不同。

## 部署 Azure OpenAI 基礎設施

**Bash:**
```bash
cd 01-introduction
azd up  # 選擇訂閱和位置（建議使用 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 選擇訂閱及地區（建議使用 eastus2）
```

> **注意：** 若遇到逾時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），請重複執行 `azd up`。Azure 資源可能仍在背景部署，重試可以讓部署在資源進入穩定狀態後完成。

此操作會：
1. 部署含 GPT-5.2 及 text-embedding-3-small 模型的 Azure OpenAI 資源
2. 自動在專案根目錄產生帶有憑證的 `.env` 檔
3. 設定所有需要的環境變數

**部署遇到問題？** 請參閱 [Infrastructure README](infra/README.md) 取得詳細疑難排解，包含子網域名稱衝突、手動 Azure Portal 部署步驟以及模型設定指引。

**確認部署成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等等。
```

> **注意：** `azd up` 指令會自動產生 `.env` 檔。如需後續更新，可手動編輯 `.env` 檔，或透過下列指令重新產生：
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

## 在本地執行應用程式

**確認部署：**

確定根目錄存在含 Azure 憑證的 `.env` 檔：

**Bash:**
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**選項 1：使用 Spring Boot 控制台（建議 VS Code 使用者）**

開發容器內建 Spring Boot 控制台擴充功能，提供管理所有 Spring Boot 應用程式的視覺介面。可在 VS Code 左側活動列找到（尋找 Spring Boot 圖示）。

在 Spring Boot 控制台，你可以：
- 查看工作區所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 即時查看應用程式日誌
- 監控應用程式狀態

點擊 "introduction" 旁的播放按鈕啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 指令**

啟動所有網頁應用程式（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 從根目錄開始
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

兩個指令腳本會自動從根目錄 `.env` 載入環境變數，且若 JAR 檔不存在會自動編譯。

> **注意：** 若你偏好先手動編譯所有模組，再啟動：
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

用瀏覽器開啟 http://localhost:8080 。

**停止應用程式：**

**Bash:**
```bash
./stop.sh  # 僅限此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

此應用提供兩種聊天功能的網頁介面並排顯示。

<img src="../../../translated_images/zh-TW/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*儀表板顯示簡單聊天（無狀態）與對話聊天（有狀態）選項*

### 無狀態聊天（左側面板）

先嘗試此模式。請先說「My name is John」，接著立刻問「What's my name?」。模型不會記得，因為每則訊息都獨立處理。這說明基本語言模型整合中的核心問題 — 缺乏對話上下文。

<img src="../../../translated_images/zh-TW/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不會記得你之前說的名字*

### 有狀態聊天（右側面板）

現在在這裡嘗試相同順序。說「My name is John」後，再問「What's my name?」這次它記得了。差異在於 MessageWindowChatMemory — 它保留對話歷史並隨每次請求一起送出。這就是生產對話式 AI 的運作方式。

<img src="../../../translated_images/zh-TW/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 記得你之前的名字*

兩個面板使用相同 GPT-5.2 模型。唯一差別是記憶功能。這清楚展現記憶對應用程式的重要性，以及為何它對真實場域不可或缺。

## 下一步

**下一模組：** [02-prompt-engineering - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導航：** [← 上一篇：Module 00 - 快速入門](../00-quick-start/README.md) | [回主頁](../README.md) | [下一篇：Module 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們努力追求準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。請以原始語言文件為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或錯誤詮釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->