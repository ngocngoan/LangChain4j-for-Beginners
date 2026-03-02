# Module 01：LangChain4j 入門

## 目錄

- [影片導覽](../../../01-introduction)
- [你將學到什麼](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [理解核心問題](../../../01-introduction)
- [認識 Tokens](../../../01-introduction)
- [記憶的運作方式](../../../01-introduction)
- [如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎架構](../../../01-introduction)
- [本地執行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [接下來的步驟](../../../01-introduction)

## 影片導覽

觀看這段直播，說明如何開始這個模組：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 你將學到什麼

如果你完成了快速入門，你已經知道如何發送提示並取得回應。這是基礎，但真正的應用需要更多功能。本模組將教你如何建立會記住上下文並維持狀態的對話 AI —— 這是一次性展示與可用於生產的應用程式間的差異。

本指南將全程使用 Azure OpenAI 的 GPT-5.2，因為其先進的推理能力讓不同模式的行為更加明顯。當你加入記憶功能時，差異會非常清楚。這讓你更容易理解每個組件如何為應用增添價值。

你將建立一個展示兩種模式的應用程式：

**無狀態聊天** - 每次請求都是獨立的。模型不會記住先前的訊息。這就是你在快速入門中使用的模式。

**有狀態對話** - 每個請求都包含對話歷史。模型會在多輪對話中維持上下文。這是生產應用所需要的。

## 先決條件

- 擁有 Azure 訂閱並能存取 Azure OpenAI
- Java 21、Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** Java、Maven、Azure CLI 和 Azure Developer CLI (azd) 均已預安裝於提供的開發容器中。

> **注意：** 本模組使用 Azure OpenAI 的 GPT-5.2。部署會透過 `azd up` 自動設定 —— 請勿修改程式碼中的模型名稱。

## 理解核心問題

語言模型是無狀態的。每次 API 調用都是獨立的。如果你先發送「我的名字是 John」，接著問「我的名字是什麼？」，模型不會知道你剛剛自我介紹。它把每個請求都當作是第一次對話。

這對簡單問答不成問題，但對真正的應用程式無用。客服機器人需要記住你告訴它的事。個人助理需要上下文。任何多輪對話都需要記憶。

<img src="../../../translated_images/zh-TW/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*無狀態（獨立呼叫）與有狀態（上下文感知）對話的差異*

## 認識 Tokens

在深入對話前，了解 tokens 非常重要 —— 它們是語言模型處理的基本文字單位：

<img src="../../../translated_images/zh-TW/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*文字如何拆解成 tokens 的範例 —— 「I love AI!」變成 4 個獨立處理單元*

Tokens 是 AI 模型衡量與處理文字的方式。字詞、標點甚至空格都可能是 tokens。你的模型一次能處理的 token 有上限（GPT-5.2 為 400,000 個，最多 272,000 輸入 tokens 與 128,000 輸出 tokens）。了解 tokens 有助於管理對話長度與成本。

## 記憶的運作方式

聊天記憶解決了無狀態問題，它會維持對話歷史。在送出你的請求到模型前，框架會先附加相關的先前訊息。當你問「我的名字是什麼？」時，系統事實上是將整個對話歷史送出，讓模型看到你之前說過「我的名字是 John」。

LangChain4j 提供了自動處理此事的記憶實作。你可以設定要保留多少訊息，框架會管理上下文視窗。

<img src="../../../translated_images/zh-TW/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 維持最近訊息的滑動視窗，自動丟棄舊訊息*

## 如何使用 LangChain4j

此模組擴充快速入門，整合了 Spring Boot 並增加對話記憶。整體架構如下：

**依賴項目** - 加入兩個 LangChain4j 函式庫：

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

**聊天模型** - 將 Azure OpenAI 配置為 Spring bean（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

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

此構建程式會從 `azd up` 設定的環境變數讀取認證。將 `baseUrl` 設成你的 Azure 端點可讓 OpenAI 用戶端搭配 Azure OpenAI 使用。

**對話記憶** - 利用 MessageWindowChatMemory 追蹤聊天歷史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

使用 `withMaxMessages(10)` 建立記憶儲存最近 10 筆訊息。以類型包裝器加入使用者訊息和 AI 訊息：`UserMessage.from(text)` 和 `AiMessage.from(text)`。以 `memory.messages()` 取回歷史並傳給模型。服務為每組對話 ID 儲存獨立記憶實例，允許多個使用者同時聊天。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打開 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ，並詢問：
> - 「當視窗已滿，MessageWindowChatMemory 如何決定要丟棄哪些訊息？」
> - 「我可以用資料庫實作自訂的記憶儲存，而非記憶體中嗎？」
> - 「要如何加入摘要功能來壓縮舊的對話歷史？」

無狀態聊天端點完全跳過記憶 —— 就像快速入門一樣只執行 `chatModel.chat(prompt)`。有狀態端點則將訊息加入記憶，取回歷史，並加上上下文一起傳送。模型設定相同，但模式不同。

## 部署 Azure OpenAI 基礎架構

**Bash：**
```bash
cd 01-introduction
azd up  # 選擇訂閱和位置（建議使用 eastus2）
```

**PowerShell：**
```powershell
cd 01-introduction
azd up  # 選擇訂閱與位置（建議使用 eastus2）
```

> **注意：** 如果發生逾時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），請再次執行 `azd up`。Azure 資源可能仍在背景佈建中，重試可以等待資源達到終止狀態後完成部署。

此步驟會：
1. 部署搭載 GPT-5.2 與 text-embedding-3-small 模型的 Azure OpenAI 資源
2. 自動在專案根目錄產生包含憑證的 `.env` 檔案
3. 設定所有需要的環境變數

**部署遇到問題？** 請查看 [Infrastructure README](infra/README.md) ，其中詳細列出子網域名稱衝突、手動 Azure Portal 部署步驟及模型設定指引。

**確認部署成功：**

**Bash：**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell：**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 指令會自動生成 `.env` 檔案。若日後需要更新，可手動編輯 `.env` 檔或透過執行以下命令重新生成：
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

確保根目錄存在帶有 Azure 憑證的 `.env` 檔：

**Bash：**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell：**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**選項 1：使用 Spring Boot 儀表板（建議 VS Code 使用者）**

開發容器已包含 Spring Boot 儀表板擴充，提供圖形介面管理所有 Spring Boot 應用程式。其位於 VS Code 左側的活動列（尋找 Spring Boot 圖示）。

從 Spring Boot 儀表板你可以：
- 查看工作區內所有可用的 Spring Boot 應用程式
- 一鍵啟動或停止應用程式
- 實時查看應用程式日誌
- 監控應用程式狀態

點擊「introduction」旁的播放按鈕即可啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有 Web 應用程式（模組 01-04）：

**Bash：**
```bash
cd ..  # 從根目錄
./start-all.sh
```

**PowerShell：**
```powershell
cd ..  # 從根目錄開始
.\start-all.ps1
```

或只啟動此模組：

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

兩支腳本都會自動從根目錄 `.env` 載入環境變數，若 JAR 不存在會自動編譯。

> **注意：** 若你偏好先手動編譯所有模組，再啟動：
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

在瀏覽器開啟 http://localhost:8080 。

**停止應用程式：**

**Bash：**
```bash
./stop.sh  # 僅此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell：**
```powershell
.\stop.ps1  # 僅限此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

應用程式提供網頁介面，並排顯示兩種聊天實作。

<img src="../../../translated_images/zh-TW/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*介面展示同時包含簡易聊天（無狀態）與對話聊天（有狀態）選項*

### 無狀態聊天（左側面板）

先試試這個。在左側輸入「我的名字是 John」，緊接著問「我的名字是什麼？」模型不會記得，因為每則訊息獨立處理。這展示了基本語言模型整合的核心問題 —— 沒有對話上下文。

<img src="../../../translated_images/zh-TW/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不記得你前一則訊息的名字*

### 有狀態聊天（右側面板）

現在在右側試同樣的序列。「我的名字是 John」然後問「我的名字是什麼？」這次模型會記得。差異在於 MessageWindowChatMemory —— 它維持對話歷史並隨每次請求送出。這就是生產對話 AI 的運作方式。

<img src="../../../translated_images/zh-TW/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 記得對話早先你說過的名字*

兩側都使用同一個 GPT-5.2 模型。唯一差別是記憶功能。這讓你明白記憶為應用帶來什麼，以及為什麼它對真實使用情境非常重要。

## 接下來的步驟

**下一模組：** [02-prompt-engineering - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導覽：** [← 前一篇：Module 00 - Quick Start](../00-quick-start/README.md) | [回主頁](../README.md) | [下一篇：Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我們努力確保準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生之任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->