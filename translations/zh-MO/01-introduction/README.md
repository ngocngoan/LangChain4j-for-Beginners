# Module 01: 開始使用 LangChain4j

## 目錄

- [影片導覽](../../../01-introduction)
- [您將學到的內容](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [理解核心問題](../../../01-introduction)
- [理解 Tokens](../../../01-introduction)
- [記憶如何運作](../../../01-introduction)
- [這如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎架構](../../../01-introduction)
- [在本地運行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [後續步驟](../../../01-introduction)

## 影片導覽

觀看這個現場教學影片，說明如何開始本模組：[Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## 您將學到的內容

如果您完成了快速入門，就會知道如何發送提示並獲取回應。這是基礎，但真實的應用程式需要更多。本模組教您如何建立會記住上下文並維持狀態的對話式 AI —— 這是一次性示範和生產就緒應用程式的差別。

本指南中將使用 Azure OpenAI 的 GPT-5.2，因其先進推理能力使不同模式的行為更明顯。當您加入記憶時，會清楚看到差異，這讓您更容易理解每個元件為應用程式帶來的價值。

您將建立一個示範兩種模式的應用程式：

**無狀態聊天** - 每個請求獨立。模型不記得先前訊息。這是快速入門中使用的模式。

**有狀態對話** - 每個請求包含對話歷史。模型能跨多輪對話維持上下文。這是生產應用所需。

## 先決條件

- 具有 Azure OpenAI 訪問權限的 Azure 訂閱
- Java 21，Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** Java、Maven、Azure CLI 和 Azure Developer CLI (azd) 已在提供的開發容器中預先安裝。

> **注意：** 本模組使用 Azure OpenAI 的 GPT-5.2。部署透過 `azd up` 自動配置 —— 請勿修改程式碼中的模型名稱。

## 理解核心問題

語言模型是無狀態的。每次 API 呼叫都是獨立的。如果您先發送「我叫 John」，再問「我叫甚麼名字？」，模型不會知道您剛才自我介紹了。它將所有請求視為您第一次對話。

這對簡單問答還可以，但對於真實應用無用。客服機器人需要記得您告訴它的資訊。個人助理需要上下文。任何多輪對話都需要記憶。

<img src="../../../translated_images/zh-MO/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*無狀態（獨立呼叫）與有狀態（有上下文）對話的差異*

## 理解 Tokens

開始進入對話前，理解 tokens 很重要 —— 它們是語言模型處理的基本文本單位：

<img src="../../../translated_images/zh-MO/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*如何將文字拆解成 tokens 的範例 ——「I love AI!」成為四個獨立處理單位*

tokens 是 AI 模型衡量和處理文本的方式。單字、標點符號，甚至空格都可成為 tokens。您的模型對一次可處理 tokens 數量有限制（GPT-5.2 為 400,000 令牌上限，輸入最多 272,000 令牌，輸出最多 128,000 令牌）。了解 tokens 有助於管理對話長度和成本。

## 記憶如何運作

聊天記憶解決了無狀態問題，透過維持對話歷史。在送出請求給模型前，框架會預先加上相關之前的訊息。當您問「我叫甚麼名字？」時，系統實際傳送了整段對話歷史給模型，讓它看到您之前說過「我叫 John」。

LangChain4j 提供自動處理此問題的記憶實作。您可以選擇保留多少訊息，框架自動管理上下文視窗。

<img src="../../../translated_images/zh-MO/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 維持一個最近訊息的滑動視窗，自動丟棄舊訊息*

## 這如何使用 LangChain4j

本模組擴充快速入門，整合 Spring Boot 並加入對話記憶。結構如下：

**依賴項** — 加入兩個 LangChain4j 函式庫：

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

**聊天模型** — 以 Spring bean 方式設定 Azure OpenAI ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java))：

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

建構器會從 `azd up` 設定的環境變數讀取憑證。將 `baseUrl` 設為您的 Azure 端點，讓 OpenAI 客戶端能和 Azure OpenAI 一起運作。

**對話記憶** — 用 MessageWindowChatMemory 跟踪聊天歷史 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

用 `withMaxMessages(10)` 建立記憶以保留最近 10 則訊息。用帶型別的包裝器新增使用者及 AI 訊息：`UserMessage.from(text)` 及 `AiMessage.from(text)`。用 `memory.messages()` 取出歷史並送給模型。服務根據對話 ID 儲存不同記憶實例，允許多使用者同時聊天。

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打開 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)，然後詢問：
> - 「當滑動視窗已滿時，MessageWindowChatMemory 如何決定丟棄哪些訊息？」
> - 「我可以用資料庫實作自訂記憶儲存，而非記憶體中嗎？」
> - 「如何加入摘要功能來壓縮舊對話歷史？」

無狀態聊天端點完全跳過記憶 — 只需 `chatModel.chat(prompt)`，跟快速入門一樣。有狀態端點則會把訊息加入記憶，擷取歷史並於每次請求附帶上下文。模型設定相同，但模式不同。

## 部署 Azure OpenAI 基礎架構

**Bash:**
```bash
cd 01-introduction
azd up  # 選擇訂閱和位置（建議選擇 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 選擇訂閱和地點（建議使用 eastus2）
```

> **注意：** 若遇超時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），請重跑一次 `azd up`。Azure 資源可能仍在背景佈署中，重試允許部署完成，等資源達到終端狀態。

此操作會：
1. 部署搭載 GPT-5.2 和 text-embedding-3-small 模型的 Azure OpenAI 資源
2. 自動在專案根目錄產生帶憑證的 `.env` 檔案
3. 設定所有所需環境變數

**部署有問題？** 請參考[infrastructure README](infra/README.md)，詳細故障排除包括子網域名稱衝突、手動 Azure 入口網站部署步驟及模型設定指引。

**驗證部署成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 命令會自動生成 `.env` 檔案。若日後需要更新，可手動編輯 `.env` 檔案，或重新執行：
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

確認根目錄有包含 Azure 憑證的 `.env` 檔案：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**方案一：使用 Spring Boot 儀表板（VS Code 使用者推薦）**

該開發容器已包含 Spring Boot 儀表板擴充套件，提供一個視覺介面管理所有 Spring Boot 應用。可在 VS Code 左側活動列找到該圖示（尋找 Spring Boot 標誌）。

在 Spring Boot 儀表板您可：
- 查看工作區中所有可用 Spring Boot 應用
- 一鍵啟動或停止應用
- 實時檢視應用日誌
- 監控應用狀態

點擊「introduction」旁的播放按鈕即可啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**方案二：使用 shell 腳本**

啟動所有 Web 應用（模組 01-04）：

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

兩個腳本會自動從根目錄 `.env` 檔案載入環境變數，且若 JAR 檔不存在則自動編譯。

> **注意：** 若您希望手動編譯所有模組後再啟動：
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

在瀏覽器開啟 http://localhost:8080 。

**停止服務：**

**Bash:**
```bash
./stop.sh  # 僅此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只有此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

應用程式提供一個網頁介面，並排顯示兩個聊天實作。

<img src="../../../translated_images/zh-MO/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*展示簡單聊天（無狀態）與對話聊天（有狀態）選項的控制面板*

### 無狀態聊天（左側面板）

先試試這個。輸入「我叫 John」然後立刻問「我叫甚麼名字？」模型不會記得，因為每則訊息是獨立處理。這展示基本語言模型集成的核心問題 — 沒有對話上下文。

<img src="../../../translated_images/zh-MO/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 沒有記住你上一則訊息中的名字*

### 有狀態聊天（右側面板）

現在在這裡試同樣的流程。輸入「我叫 John」然後「我叫甚麼名字？」這次模型會記得。差別在於 MessageWindowChatMemory —— 它維持對話歷史並於每次請求時附加上下文。這就是生產對話式 AI 的方式。

<img src="../../../translated_images/zh-MO/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 記得早先對話中的名字*

兩個面板使用相同 GPT-5.2 模型。唯一差異是記憶。這清楚說明記憶為應用帶來什麼，以及為何真實應用必需它。

## 後續步驟

**下一模組：** [02-prompt-engineering - GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導航：** [← 上一篇：Module 00 - 快速入門](../00-quick-start/README.md) | [返回主頁](../README.md) | [下一篇：Module 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始語言版本文件應被視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->