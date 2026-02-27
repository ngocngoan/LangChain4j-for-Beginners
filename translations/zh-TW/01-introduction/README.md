# Module 01: 使用 LangChain4j 快速開始

## 目錄

- [影片演練](../../../01-introduction)
- [您將學到什麼](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [理解核心問題](../../../01-introduction)
- [理解 Token](../../../01-introduction)
- [記憶如何運作](../../../01-introduction)
- [本課程如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎架構](../../../01-introduction)
- [在本機執行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [後續步驟](../../../01-introduction)

## 影片演練

觀看這場直播課程，說明如何開始使用本模組：[使用 LangChain4j 快速開始 - 直播課程](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## 您將學到什麼

如果您完成了快速開始，您會知道如何送出提示並取得回應。這是基礎，但實際應用需要更多功能。本模組將教您如何構建會記憶上下文且能維持狀態的對話式 AI — 這正是區分單次展示與生產就緒應用的關鍵。

本文將全程使用 Azure OpenAI 的 GPT-5.2，因其強大的推理能力讓各種模式間行為更易辨識。加入記憶功能後，您將清楚看到差異，使您更容易理解每個元件對應用的貢獻。

您將打造一套演示這兩種模式的應用程式：

**無狀態聊天** — 每次請求獨立，模型不記得之前的訊息。這是您在快速開始時所用的模式。

**有狀態對話** — 每次請求都包含對話歷史，模型可保持跨多輪的上下文。這是生產應用所需的模式。

## 先決條件

- 具備 Azure 訂閱及 Azure OpenAI 存取權
- Java 21、Maven 3.9 以上
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的開發容器中已預裝 Java、Maven、Azure CLI 及 Azure Developer CLI（azd）。

> **注意：** 本模組使用 Azure OpenAI 上的 GPT-5.2。部署透過 `azd up` 自動設定，請勿修改程式碼中的模型名稱。

## 理解核心問題

語言模型是無狀態的。每次 API 呼叫都是獨立的。如果你先說「我叫約翰」，然後問「我叫什麼名字？」，模型並不知道你剛自我介紹過。它將每個請求視為第一次對話。

這對簡單的問答沒問題，但對實際應用無用。客服機器人需要記住你說過的話。個人助理需要上下文。任何多輪對話都需要記憶。

<img src="../../../translated_images/zh-TW/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*無狀態（獨立呼叫）與有狀態（具上下文）的對話差異*

## 理解 Token

在深入對話之前，了解 Token 非常重要 — 它是語言模型處理文字的基本單位：

<img src="../../../translated_images/zh-TW/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*文字如何被拆分成 Token 的範例 — 「I love AI!」分成 4 個處理單位*

Token 是 AI 模型衡量與處理文字的方法。單字、標點甚至空格都可以是 Token。模型一次可處理的 Token 有上限（GPT-5.2 為 400,000 個，輸入最多 272,000，輸出最多 128,000）。理解 Token 有助於您管理對話長度和成本。

## 記憶如何運作

聊天記憶藉由維持對話歷史解決無狀態問題。發送請求給模型前，框架會加上相關的先前訊息。當您問「我叫什麼名字？」時，實際上系統會送出整段對話歷史，讓模型看到您先前說過「我叫約翰」。

LangChain4j 提供的記憶實作能自動處理這些。您可以設定保留訊息數量，框架會管理上下文視窗。

<img src="../../../translated_images/zh-TW/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 維持最近訊息的滑動視窗，自動捨棄舊訊息*

## 本課程如何使用 LangChain4j

本模組在快速開始基礎上整合 Spring Boot 並新增對話記憶。整體組成如下：

**相依項** — 新增兩個 LangChain4j 函式庫：

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

**聊天模型** — 將 Azure OpenAI 配置為 Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java))：

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

建構器從 `azd up` 設定的環境變數讀取憑證。設定 `baseUrl` 為您的 Azure 端點，使 OpenAI 用戶端能與 Azure OpenAI 服務配合。

**對話記憶** — 使用 MessageWindowChatMemory 跟蹤聊天歷史 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

用 `withMaxMessages(10)` 建立記憶，以保留最近 10 則訊息。使用型別包裝來新增使用者及 AI 訊息：`UserMessage.from(text)` 和 `AiMessage.from(text)`。用 `memory.messages()` 取得歷史並送給模型。服務會為每個會話 ID 保留獨立記憶實例，支援多使用者同時聊天。

> **🤖 試試 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)，並試問：
> - 「MessageWindowChatMemory 在視窗滿時如何決定要丟棄哪些訊息？」
> - 「我能否用資料庫實作自訂記憶存儲，而非記憶體中方式？」
> - 「要怎麼加上摘要功能，壓縮舊的對話歷史？」

無狀態聊天端點則完全跳過記憶，只用 `chatModel.chat(prompt)`，類似快速開始。狀態端點會將訊息加入記憶、取得歷史，並將上下文隨每次請求帶入。相同模型配置、不同模式。

## 部署 Azure OpenAI 基礎架構

**Bash:**
```bash
cd 01-introduction
azd up  # 選擇訂閱和位置（建議使用 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 選擇訂閱和地點（建議使用 eastus2）
```

> **注意：** 遇到逾時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`）時，只需再次執行 `azd up`。Azure 資源可能仍在背景佈署，重試可讓部署在資源達到終止狀態後完成。

此部署將：
1. 部署 Azure OpenAI 資源，包含 GPT-5.2 與 text-embedding-3-small 模型
2. 自動在專案根目錄產生 `.env` 憑證檔
3. 設定所有必要的環境變數

**部署遇到問題？** 請參考 [基礎架構 README](infra/README.md) ，查閱詳細疑難排解，包括子網域名稱衝突、Azure Portal 手動部署步驟以及模型配置指引。

**驗證部署成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等等。
```

> **注意：** `azd up` 指令會自動建立 `.env` 文件。若之後要更新，可手動編輯 `.env` 或用以下指令重新生成：
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```

> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```


## 在本機執行應用程式

**驗證部署：**

請確定根目錄存在 `.env` 檔，包含 Azure 憑證：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**選項 1：使用 Spring Boot 儀表板（建議 VS Code 使用者）**

開發容器內建 Spring Boot 儀表板延伸模組，可視化管理所有 Spring Boot 應用。可於 VS Code 左側活動列找到 Spring Boot 圖標。

在儀表板中，您可：
- 查看工作區所有可用 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時瀏覽應用日誌
- 監控應用狀態

只需點選「introduction」旁播放按鈕啟動本模組，或同時啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有 Web 應用（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄開始
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

兩個腳本會自動從根目錄 `.env` 載入環境變數，如尚未建置 JAR，會自動建置。

> **注意：** 若想先手動編譯所有模組再啟動：
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

於瀏覽器開啟 http://localhost:8080 。

**停止應用：**

**Bash:**
```bash
./stop.sh  # 僅此模組
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

本應用提供並排的兩種聊天功能網站介面。

<img src="../../../translated_images/zh-TW/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*儀表板顯示簡易聊天（無狀態）與對話聊天（有狀態）選項*

### 無狀態聊天（左側面板）

請先嘗試。先說「我叫約翰」，然後立刻問「我叫什麼名字？」模型不會記得，因為每則消息都是獨立的。這演示基本語言模型集成的核心問題 — 無對話上下文。

<img src="../../../translated_images/zh-TW/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不記得您之前訊息中的名字*

### 有狀態聊天（右側面板）

現在同樣順序試試這一側。先說「我叫約翰」，再問「我叫什麼名字？」這次它記得了。差別在 MessageWindowChatMemory — 它維持對話歷史，並隨每次請求一併送出。這就是生產級對話 AI 的運作方式。

<img src="../../../translated_images/zh-TW/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 記得對話中先前說過的名字*

兩邊都使用相同的 GPT-5.2 模型。唯一差異是記憶功能。這讓您清楚了解記憶對應用的重要性，以及為何實務使用必須具備。

## 後續步驟

**下一模組：** [02-prompt-engineering - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導覽：** [← 上一章：模組 00 - 快速開始](../00-quick-start/README.md) | [回主頁](../README.md) | [下一章：模組 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保翻譯的準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始語言版本的文件應視為權威來源。對於重要資訊，建議尋求專業人工翻譯。本公司不對因使用本翻譯所引起的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->