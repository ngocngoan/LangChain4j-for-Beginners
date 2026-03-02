# Module 01: LangChain4j 入門

## 目錄

- [影片導覽](../../../01-introduction)
- [你將學到什麼](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [理解核心問題](../../../01-introduction)
- [理解 Tokens](../../../01-introduction)
- [記憶是如何運作的](../../../01-introduction)
- [如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎架構](../../../01-introduction)
- [在本機執行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [下一步](../../../01-introduction)

## 影片導覽

觀看這場直播，說明如何開始使用本模組：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 你將學到什麼

在快速入門中，你使用 GitHub Models 傳送提示、呼叫工具、建立 RAG 流程並測試保護措施。這些展示了可能的方式 — 現在我們轉向 Azure OpenAI 和 GPT-5.2，開始建立生產樣式的應用程式。本模組專注於會記得上下文並維持狀態的對話式 AI — 這些概念在快速入門的示範中有用到但沒有詳細說明。

本指南全程使用 Azure OpenAI 的 GPT-5.2，因為其先進的推理能力讓不同模式的行為更明顯。加入記憶後，你會清楚看到差異。這讓你更容易理解每個元件為你的應用帶來什麼。

你將建立一個示範兩種模式的應用程式：

**無狀態聊天** — 每次請求都是獨立的。模型不會記得之前的訊息。這是你在快速入門中使用的模式。

**有狀態對話** — 每次請求包含對話歷史。模型能跨多輪維持上下文。這是生產應用需要的模式。

## 先決條件

- 具有 Azure OpenAI 使用權限的 Azure 訂閱
- Java 21、Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的 devcontainer 預先安裝了 Java、Maven、Azure CLI 和 Azure Developer CLI (azd)。

> **注意：** 本模組使用 Azure OpenAI 上的 GPT-5.2。部署是透過 `azd up` 自動設定，請不要修改程式碼中的模型名稱。

## 理解核心問題

語言模型是無狀態的。每次 API 呼叫都是獨立的。如果你送出「我的名字是約翰」，然後問「我的名字是什麼？」模型根本不知道你剛剛自我介紹了。它把每次請求當作你第一次對話。

這對於簡單問答沒問題，但對真正應用沒用。客服機器人需要記得你跟它說過什麼。個人助理需要上下文。任何多輪對話都需要記憶。

下面的圖示對比兩種方式 — 左側是無狀態呼叫，忘記你的名字；右側是有狀態呼叫，使用 ChatMemory 記得你的名字。

<img src="../../../translated_images/zh-TW/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*無狀態（獨立呼叫）與有狀態（上下文感知）對話的差別*

## 理解 Tokens

在深入對話前，了解 tokens 很重要 — tokens 是語言模型處理的基本文字單元：

<img src="../../../translated_images/zh-TW/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*文字如何被拆成 tokens 範例 — 「I love AI!」變成 4 個獨立的處理單元*

tokens 是 AI 模型衡量和處理文字的方式。詞語、標點甚至空格都可能是 tokens。你的模型有一次能處理的 token 數量上限（GPT-5.2 為 400,000，含最多 272,000 輸入 tokens 與 128,000 輸出 tokens）。了解 tokens 有助管理對話長度和成本。

## 記憶是如何運作的

聊天記憶透過維持對話歷史解決無狀態問題。在送請求給模型前，框架會將相關的先前訊息加到前面。當你問「我的名字是什麼？」系統實際上是送出整段對話歷史，讓模型看到你之前說了「我的名字是約翰」。

LangChain4j 提供自動處理此事的記憶實作。你選擇保留幾則訊息，框架管理上下文視窗。下面圖示顯示 MessageWindowChatMemory 如何管理近期訊息的滑動視窗。

<img src="../../../translated_images/zh-TW/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 維持近期訊息的滑動視窗，自動捨棄較舊的訊息*

## 如何使用 LangChain4j

本模組在快速入門基礎上整合 Spring Boot 並加入對話記憶。組件搭配如下：

**依賴項** — 新增兩個 LangChain4j 函式庫：

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

**聊天模型** — 以 Spring bean 配置 Azure OpenAI（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

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

建構器從 `azd up` 設定的環境變數讀取認證。設定 `baseUrl` 為你的 Azure 端點，使 OpenAI 用戶端可與 Azure OpenAI 一起使用。

**對話記憶** — 使用 MessageWindowChatMemory 追蹤聊天歷史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

用 `withMaxMessages(10)` 建立記憶，保留最近 10 則訊息。用型別包裝器新增使用者與 AI 訊息：`UserMessage.from(text)` 與 `AiMessage.from(text)`。用 `memory.messages()` 取得歷史並送給模型。服務依對話 ID 保存獨立記憶，允許多使用者同時聊天。

> **🤖 可嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：打開 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) 並問：**
> - 「MessageWindowChatMemory 在視窗滿時如何決定要丟棄哪些訊息？」
> - 「我能用資料庫實作自訂記憶存儲嗎，取代記憶體？」
> - 「要如何加入摘要功能以壓縮舊的對話歷史？」

無狀態聊天端點完全跳過記憶 — 就像快速入門的 `chatModel.chat(prompt)`。有狀態端點則是將訊息加入記憶、取得歷史，並在每次請求時包含該上下文。模型配置相同，模式不同。

## 部署 Azure OpenAI 基礎架構

**Bash:**
```bash
cd 01-introduction
azd up  # 選擇訂閱和位置（建議使用 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 選擇訂閱和位置（建議使用 eastus2）
```

> **注意：** 若遇到逾時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），只要再執行一次 `azd up` 即可。Azure 資源可能仍在背景部署，再試一次能讓部署在資源到達終止狀態後順利完成。

這將會：
1. 部署 Azure OpenAI 資源，包含 GPT-5.2 與 text-embedding-3-small 模型
2. 自動在專案根目錄產生 `.env` 憑證檔案
3. 設定所有需要的環境變數

**部署遇到困難？** 請參考 [Infrastructure README](infra/README.md) 取得詳細疑難排解，包含子網域名稱衝突、手動 Azure Portal 部署步驟與模型設定指引。

**確認部署成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 命令會自動生成 `.env` 檔案。若要之後更新，可手動編輯 `.env` 檔案或重新執行：
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

## 在本機執行應用程式

**確認部署狀態：**

確保 `.env` 檔案存在根目錄且含有 Azure 認證。於模組目錄（`01-introduction/`）執行：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**選項 1：使用 Spring Boot Dashboard（推薦給 VS Code 使用者）**

開發容器內含 Spring Boot Dashboard 擴充套件，提供管理所有 Spring Boot 應用的視覺介面。可在 VS Code 的側邊欄活動列找到（尋找 Spring Boot 圖示）。

在 Spring Boot Dashboard，你可以：
- 查看工作區內所有 Spring Boot 應用程式
- 一鍵啟動／停止應用程式
- 實時查看應用日誌
- 監控應用狀態

只要點選「introduction」旁的播放按鈕即可啟用此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot Dashboard — 從同一位置啟動、停止及監控所有模組*

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

這兩個腳本會自動從根目錄 `.env` 載入環境變數，且如果不存在 JAR 檔會先編譯。

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

於瀏覽器開啟 http://localhost:8080 。

**停止應用程式：**

**Bash:**
```bash
./stop.sh  # 僅此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅限此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

應用程式呈現一個網頁介面，左右並排兩種不同聊天實作。

<img src="../../../translated_images/zh-TW/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*主畫面展示簡易聊天（無狀態）與對話聊天（有狀態）兩種選項*

### 無狀態聊天（左側面板）

先試試看這個。問「我的名字是約翰」，緊接著問「我的名字是什麼？」模型不會記得，因為每則訊息都是獨立的。這展現了基礎語言模型整合的核心問題 — 無對話上下文。

<img src="../../../translated_images/zh-TW/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不會記得你上一句話中的名字*

### 有狀態聊天（右側面板）

現在在這裡試同樣流程。問「我的名字是約翰」，再問「我的名字是什麼？」這次模型會記得。關鍵是 MessageWindowChatMemory — 它維持對話歷史且每次請求都帶上。這就是生產環境對話式 AI 的工作方式。

<img src="../../../translated_images/zh-TW/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 記得對話早先提過的名字*

兩個面板都使用相同的 GPT-5.2 模型，唯一不同就是記憶。這讓你清楚了解記憶為應用程式帶來的價值及其對實務需求的必要性。

## 下一步

**下一模組：** [02-prompt-engineering - GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導航：** [← 上一節：模組 00 - 快速入門](../00-quick-start/README.md) | [回主頁](../README.md) | [下一節：模組 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的原文版本應視為權威來源。對於關鍵資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->