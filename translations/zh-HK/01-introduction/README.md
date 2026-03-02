# Module 01: 使用 LangChain4j 起步

## 目錄

- [影片導覽](../../../01-introduction)
- [您將學到什麼](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [了解核心問題](../../../01-introduction)
- [了解 Tokens](../../../01-introduction)
- [記憶如何運作](../../../01-introduction)
- [如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎設施](../../../01-introduction)
- [在本地運行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左面板）](../../../01-introduction)
  - [有狀態聊天（右面板）](../../../01-introduction)
- [下一步](../../../01-introduction)

## 影片導覽

觀看這段直播介紹，說明如何開始本模組：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="使用 LangChain4j 起步 - 直播" width="800"/></a>

## 您將學到什麼

如果您完成快速入門，就會知道如何送出提示並獲取回應。那是基礎，但真正的應用需要更多。本模組教您如何建立能記憶上下文和維持狀態的對話式 AI —— 這是一次性展示與生產就緒應用之間的差異。

我們將全程使用 Azure OpenAI 的 GPT-5.2，因為其先進的推理能力讓不同模式的行為更明顯。當您加入記憶時，差異會更加清楚。這讓您更易理解每個元件為應用帶來的價值。

您將建立一個示範兩種模式的應用程式：

**無狀態聊天** — 每次請求皆獨立。模型不會記得之前的訊息。這是您在快速入門中使用的模式。

**有狀態對話** — 每次請求皆包含對話歷史。模型會在多輪中保持上下文。這是生產應用所需的。

## 先決條件

- 有 Azure 訂閱並可使用 Azure OpenAI
- Java 21，Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的開發容器已預先安裝 Java、Maven、Azure CLI 與 Azure Developer CLI (azd)。

> **注意：** 本模組使用 Azure OpenAI 上的 GPT-5.2。透過 `azd up` 自動配置部署，請勿更改程式碼中的模型名稱。

## 了解核心問題

語言模型是無狀態的。每次 API 呼叫都是獨立的。如果你先說「我叫 John」，然後問「我叫什麼名字？」，模型並不知道你剛才自我介紹過。它會將每個請求當作第一次對話來處理。

這對簡單問答沒問題，但對真實應用毫無用處。客服機器人必須記住您告訴它的事。個人助理需要上下文。任何多輪對話都需要記憶。

<img src="../../../translated_images/zh-HK/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="無狀態 vs 有狀態對話" width="800"/>

*無狀態（獨立呼叫）與有狀態（具上下文）的對話差異*

## 了解 Tokens

開始對話之前，了解 tokens 是必要的——它是語言模型處理的基本文字單位：

<img src="../../../translated_images/zh-HK/token-explanation.c39760d8ec650181.webp" alt="Tokens 說明" width="800"/>

*示例：文字如何被分割為 tokens —— 「I love AI!」分成 4 個處理單位*

Tokens 是 AI 模型如何衡量與處理文字的方式。詞彙、標點符號，甚至空格都可能是 token。您的模型有最大可處理的 token 數量限制（GPT-5.2 是 400,000 個，包含最多 272,000 輸入 token 及 128,000 輸出 token）。了解 tokens 能幫助您管理對話長度與成本。

## 記憶如何運作

聊天記憶透過維持對話歷史解決無狀態問題。在送出請求之前，框架會附加相關的先前訊息。當你問「我叫什麼名字？」時，系統其實送出整個對話歷史，讓模型看到你之前說「我叫 John」。

LangChain4j 提供記憶的實做，會自動處理這些。您可設定保留訊息數量，框架會管理上下文視窗。

<img src="../../../translated_images/zh-HK/memory-window.bbe67f597eadabb3.webp" alt="記憶視窗概念" width="800"/>

*MessageWindowChatMemory 維持最近訊息的滑動視窗，會自動丟棄最舊訊息*

## 如何使用 LangChain4j

此模組延伸快速入門，整合 Spring Boot 並新增對話記憶。這些元件如何串接：

**依賴項** — 增加兩個 LangChain4j 函式庫：

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

**聊天模型** — 設定 Azure OpenAI 為 Spring bean（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

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

建構器會從 `azd up` 設定的環境變數讀取認證。設定 `baseUrl` 為您的 Azure 端點，使 OpenAI 用戶端可以與 Azure OpenAI 配合運作。

**對話記憶** — 使用 MessageWindowChatMemory 管理聊天歷史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

用 `withMaxMessages(10)` 建立記憶，只保留最近 10 則訊息。使用類型包裝新增用戶與 AI 訊息：`UserMessage.from(text)` 和 `AiMessage.from(text)`。用 `memory.messages()` 取得歷史，並送給模型。服務會針對每組對話 ID 儲存不同記憶實例，允許多用戶同時聊天。

> **🤖 試試 GitHub Copilot Chat:** 打開 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)，嘗試問：
> - 「MessageWindowChatMemory 在視窗滿時如何決定丟棄哪些訊息？」
> - 「我可以用資料庫實作自訂的記憶儲存，取代記憶體儲存嗎？」
> - 「要如何加入摘要功能，壓縮舊的對話歷史？」

無狀態聊天端點完全跳過記憶 —— 直接用 `chatModel.chat(prompt)`，和快速入門一樣。有狀態端點會新增訊息到記憶，取得歷史，並將上下文與每次請求一同送出。模型配置相同，使用模式不同。

## 部署 Azure OpenAI 基礎設施

**Bash:**
```bash
cd 01-introduction
azd up  # 選擇訂閱和位置（建議使用 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 選擇訂閱和位置（建議選擇 eastus2）
```

> **注意：** 如果遇到逾時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），只要重新執行 `azd up`。Azure 資源可能仍在後台建立中，重試可讓部署在資源達成終止狀態時完成。

此操作會：
1. 部署搭載 GPT-5.2 和 text-embedding-3-small 模型的 Azure OpenAI 資源
2. 自動在專案根目錄生成 `.env` 檔並包含憑證
3. 設定所有所需的環境變數

**部署有困難？** 請參閱 [Infrastructure README](infra/README.md) 獲得詳細故障排除資訊，包括子網域名稱衝突、手動 Azure 入口網站部署步驟與模型配置指引。

**確認部署成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 指令會自動產生 `.env` 檔。日後若需更新，您可以直接手動編輯 `.env`，或透過以下指令重新產生：
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

**確認部署：**

確保根目錄有 `.env` 檔且包含 Azure 認證：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**選項 1：使用 Spring Boot Dashboard（推薦 VS Code 用戶）**

開發容器已安裝 Spring Boot Dashboard 擴充套件，提供可視化介面管理所有 Spring Boot 應用程式。您可在 VS Code 左側的活動列找到它（找 Spring Boot 圖示）。

透過 Spring Boot Dashboard，您可以：
- 查看工作區內所有可用的 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 即時查看應用日誌
- 監控應用狀態

只需點擊 "introduction" 旁的播放鈕，即可啟動本模組，或者一次啟動多個模組。

<img src="../../../translated_images/zh-HK/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot 控制面板" width="400"/>

**選項 2：使用 shell 腳本**

啟動所有網頁應用程式（模組 01 到 04）：

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

兩個腳本會自動從根目錄 `.env` 檔讀取環境變數，若 JAR 不存在則會自動編譯。

> **注意：** 如果您想先手動編譯所有模組再啟動：
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

於瀏覽器開啟 http://localhost:8080 。

**要停止應用程式：**

**Bash:**
```bash
./stop.sh  # 僅限此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只有呢個模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

此應用程式提供一個網頁介面，左右並排顯示兩種聊天實作。

<img src="../../../translated_images/zh-HK/home-screen.121a03206ab910c0.webp" alt="應用程式主畫面" width="800"/>

*面板展示同時提供簡易聊天（無狀態）及對話聊天（有狀態）選項*

### 無狀態聊天（左面板）

先試這個。問「我叫 John」，然後立刻問「我叫什麼名字？」模型不會記得，因為每則訊息都是獨立的。這展現基本語言模型整合的核心問題——沒有對話上下文。

<img src="../../../translated_images/zh-HK/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="無狀態聊天示範" width="800"/>

*AI 不會記得你上一則訊息提到的名字*

### 有狀態聊天（右面板）

現在在這裡試同樣操作。問「我叫 John」，接著問「我叫什麼名字？」這次它會記得。差異在於 MessageWindowChatMemory —— 它維持對話歷史，並在每次請求中加上上下文。這就是生產用對話式 AI 的作法。

<img src="../../../translated_images/zh-HK/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="有狀態聊天示範" width="800"/>

*AI 記得你在對話早先提到的名字*

兩個面板都使用同一個 GPT-5.2 模型。唯一差別是有無記憶。這清楚顯示記憶對應用的貢獻及為何真實應用必需。

## 下一步

**下一模組：** [02-prompt-engineering - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導覽：** [← 上一頁：Module 00 - 快速入門](../00-quick-start/README.md) | [回主頁](../README.md) | [下一頁：Module 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 自動翻譯。雖然我們努力追求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對使用本翻譯所引致的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->