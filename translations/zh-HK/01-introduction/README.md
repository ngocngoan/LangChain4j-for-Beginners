# Module 01: LangChain4j 入門

## 目錄

- [影片導覽](../../../01-introduction)
- [你將學會什麼](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [理解核心問題](../../../01-introduction)
- [理解 Tokens](../../../01-introduction)
- [記憶如何運作](../../../01-introduction)
- [如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎設施](../../../01-introduction)
- [本地運行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [下一步](../../../01-introduction)

## 影片導覽

觀看此現場教學，說明如何開始使用本模組：[LangChain4j 入門 - 現場教學](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## 你將學會什麼

如果你完成了快速入門，就已經看到如何發送提示並獲取回應。這是基礎，但真正的應用需要更多。本模組將教你如何建立能記住上下文和維持狀態的對話式 AI——這是一次性演示和生產準備應用之間的區別。

我們整個教學中會使用 Azure OpenAI 的 GPT-5.2，因其高階推理能力使得不同模式的行為更明顯。加入記憶功能後，你會清楚看到差別。這讓你更容易理解每個組件為你的應用帶來了什麼。

你將建立一個展示兩種模式的應用程式：

**無狀態聊天** - 每次請求皆獨立。模型不會記住先前的訊息。這是你在快速入門中使用的模式。

**有狀態對話** - 每次請求會包含對話歷史。模型會維持跨多輪的上下文。這正是生產應用所需。

## 先決條件

- 擁有 Azure 訂閱並開通 Azure OpenAI
- Java 21、Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** Java、Maven、Azure CLI 和 Azure Developer CLI (azd) 已預先安裝在提供的 devcontainer 中。

> **注意：** 本模組使用 Azure OpenAI 上的 GPT-5.2。部署透過 `azd up` 自動配置，請勿修改程式碼中的模型名稱。

## 理解核心問題

語言模型是無狀態的。每次 API 呼叫皆獨立。如果你傳送「我的名字是 John」然後問「我的名字是什麼？」，模型不知道你剛剛自我介紹過。它將每個請求視為首度對話。

這對簡單問答沒問題，但對真正的應用毫無用處。客服機器人需要記住你告訴它的內容。個人助理需要上下文。任何多輪對話都需要記憶。

<img src="../../../translated_images/zh-HK/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*無狀態（獨立呼叫）與有狀態（上下文感知）對話的差異*

## 理解 Tokens

在深入對話之前，理解 tokens 十分重要——語言模型處理文本的基本單位：

<img src="../../../translated_images/zh-HK/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*文字如何被拆解成 tokens 的範例 - 「I love AI!」變成 4 個分開的處理單位*

tokens 是 AI 模型衡量和處理文字的方式。單字、標點，甚至空白都可以是 tokens。你的模型有一次可處理的 tokens 數量上限（GPT-5.2 為 400,000 個，包含最多 272,000 輸入 tokens 和 128,000 輸出 tokens）。了解 tokens 有助於你控管對話長度與成本。

## 記憶如何運作

聊天記憶解決了無狀態問題，透過維護對話歷史。在將請求送給模型前，框架會在前方附加相關的先前訊息。當你問「我的名字是什麼？」時，系統實際上傳送整個對話歷史給模型，讓模型看到你之前說過「我的名字是 John」。

LangChain4j 提供了自動處理這件事的記憶實作。你選擇保留多少條訊息，框架管理上下文視窗。

<img src="../../../translated_images/zh-HK/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 維持一個近期訊息的滑動視窗，自動丟棄舊訊息*

## 如何使用 LangChain4j

本模組在快速入門基礎上擴充，整合 Spring Boot 並加入對話記憶。組件配合方式如下：

**依賴項** - 新增兩個 LangChain4j 函式庫：

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

**聊天模型** - 設定 Azure OpenAI 為 Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java))：

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

建構器從 `azd up` 設定的環境變數讀取憑證。將 `baseUrl` 設為你的 Azure 端點，使 OpenAI 客戶端可與 Azure OpenAI 配合運作。

**對話記憶** - 用 MessageWindowChatMemory 追蹤聊天歷史 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

以 `withMaxMessages(10)` 建立記憶，保留最近 10 則訊息。用類型包裝器加入使用者和 AI 訊息：`UserMessage.from(text)` 與 `AiMessage.from(text)`。用 `memory.messages()` 取出歷史，再送給模型。服務會根據對話 ID 存放不同記憶實例，允許多個使用者同時聊天。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：** 開啟 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) 與它對話：
> - 「MessageWindowChatMemory 在視窗已滿時如何決定丟棄哪些訊息？」
> - 「我可以用資料庫實作自訂記憶儲存，而非記憶體內存儲嗎？」
> - 「要怎麼加上摘要功能來壓縮舊的對話歷史？」

無狀態聊天端點完全跳過記憶——只執行像快速入門那樣的 `chatModel.chat(prompt)`。有狀態端點則加入訊息到記憶，取出歷史並每次請求帶上上下文。使用相同模型配置，模式不同。

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

> **注意：** 若遇見超時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），請重新執行 `azd up`。Azure 資源可能還在背景佈建中，重試能讓部署等到資源到達終止狀態後順利完成。

此操作會：
1. 部署搭載 GPT-5.2 與 text-embedding-3-small 模型的 Azure OpenAI 資源
2. 自動在專案根目錄產生帶有憑證的 `.env` 檔案
3. 設定所有必需的環境變數

**部署有問題？** 請參考 [基礎設施說明文件](infra/README.md) 以取得詳細除錯說明，包括子網域名稱衝突、手動在 Azure Portal 部署步驟及模型配置指引。

**確認部署成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 指令會自動建立 `.env` 檔案。若你之後需要更新，可以手動編輯 `.env`，或重新執行以下指令生成：
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

## 本地運行應用程式

**確認部署：**

確保專案根目錄已有 `.env` 檔案並包含 Azure 憑證：

**Bash:**
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**選項 1：使用 Spring Boot Dashboard（推薦給 VS Code 使用者）**

開發容器預裝 Spring Boot Dashboard 擴充，提供管理所有 Spring Boot 應用的視覺界面。你可在 VS Code 左側活動列找到它（尋找 Spring Boot 圖示）。

透過 Spring Boot Dashboard，你可以：
- 查看工作區內所有 Spring Boot 應用
- 一鍵啟動/停止應用程式
- 即時查看應用日誌
- 監控應用狀態

只需按下「introduction」旁的播放鈕啟動本模組，或者同時啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有 web 應用程式（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄開始
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 由根目錄開始
.\start-all.ps1
```

或者只啟動本模組：

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

兩個腳本會自動從根目錄 `.env` 載入環境變數，並在 JAR 不存在時自動編譯。

> **注意：** 若你想先手動編譯所有模組再啟動：
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

在瀏覽器打開 http://localhost:8080 。

**停止服務：**

**Bash:**
```bash
./stop.sh  # 只有這個模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

應用提供網頁界面，左、右兩種聊天實作並列展示。

<img src="../../../translated_images/zh-HK/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*儀表板展示簡易聊天（無狀態）與對話聊天（有狀態）選項*

### 無狀態聊天（左側面板）

先嘗試這個。問「我的名字是 John」然後立刻問「我的名字是什麼？」模型不會記得，因每則訊息都是獨立的。這展示了基礎語言模型整合的核心問題——沒有對話上下文。

<img src="../../../translated_images/zh-HK/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不會記得你之前提過的名字*

### 有狀態聊天（右側面板）

接著在這邊嘗試同樣的流程。說「我的名字是 John」，再問「我的名字是什麼？」這次它會記得。差別在於 MessageWindowChatMemory——它會維持對話歷史，並隨每次請求包含這些上下文。這就是生產對話式 AI 的作法。

<img src="../../../translated_images/zh-HK/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 記得你在對話早期提過的名字*

兩個面板都使用相同的 GPT-5.2 模型。唯一差別是記憶。這讓你清楚明白記憶帶給應用的價值，以及為何它對真實使用情境至關重要。

## 下一步

**下一模組：** [02-prompt-engineering - 使用 GPT-5.2 進行提示工程](../02-prompt-engineering/README.md)

---

**導航：** [← 上一章：Module 00 - 快速入門](../00-quick-start/README.md) | [返回主頁](../README.md) | [下一章：Module 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於重要資訊，建議尋求專業人工翻譯。本公司不對因使用此翻譯而導致的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->