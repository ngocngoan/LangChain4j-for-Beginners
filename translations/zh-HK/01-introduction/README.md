# Module 01: LangChain4j 入門

## 目錄

- [影片導覽](../../../01-introduction)
- [你將學到的內容](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [理解核心問題](../../../01-introduction)
- [理解 Tokens](../../../01-introduction)
- [記憶如何運作](../../../01-introduction)
- [本模組如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎設施](../../../01-introduction)
- [在本機執行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [後續步驟](../../../01-introduction)

## 影片導覽

觀看此直播教學，說明如何開始本模組：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="LangChain4j 開始使用 - 直播教學" width="800"/></a>

## 你將學到的內容

在快速入門中，你使用 GitHub Models 來發送提示、調用工具、建構 RAG 管線並測試護欄。這些示範展示了可能性—現在我們轉向 Azure OpenAI 與 GPT-5.2，開始建立生產風格的應用程式。本模組聚焦於記憶上下文並維持狀態的對話式 AI — 這是快速入門示範背後用到但沒有解釋的概念。

整個指南中我們將使用 Azure OpenAI 的 GPT-5.2，因為其進階推理能力讓不同模式的行為更明顯。加入記憶後，你會清楚看到差異。這幫助你更容易理解每個組件帶給應用的價值。

你將建置一個示範兩種模式的應用：

**無狀態聊天** — 每次請求彼此獨立。模型不記得之前的訊息。這是你在快速入門中使用過的模式。

**有狀態對話** — 每次請求都包含對話歷史。模型維持多輪對話的上下文。這才是生產應用所需。

## 先決條件

- 具備 Azure OpenAI 存取權的 Azure 訂閱
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的開發容器內已預裝 Java、Maven、Azure CLI 及 Azure Developer CLI（azd）。

> **注意：** 本模組使用 Azure OpenAI 上的 GPT-5.2。部署會透過 `azd up` 自動設定，請勿修改程式中的模型名稱。

## 理解核心問題

語言模型本身是無狀態的。每次 API 呼叫彼此獨立。如果你說「我的名字是 John」後，再問「我的名字是什麼？」，模型無法得知你剛剛自我介紹過。它將每次請求視為你第一次對話。

這對簡單問答沒問題，但對真正的應用無用。客服機器人需記得你告訴他們的訊息。個人助理需要上下文。任何多輪對話都需要記憶。

下圖比較兩種方式 — 左邊是無狀態呼叫會忘記你的名字；右邊是由 ChatMemory 支援的有狀態呼叫會記得你的名字。

<img src="../../../translated_images/zh-HK/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="無狀態與有狀態對話" width="800"/>

*無狀態（獨立呼叫）與有狀態（具上下文認知）對話的差異*

## 理解 Tokens

在深入對話前，理解 Tokens 很重要 — 這是語言模型處理文字的基本單位：

<img src="../../../translated_images/zh-HK/token-explanation.c39760d8ec650181.webp" alt="Tokens 說明" width="800"/>

*範例：文字如何被拆成 tokens — 「I love AI!」變成四個獨立處理單位*

Tokens 為 AI 模型衡量並處理文字的方式。單字、標點符號，甚至空格都可能是 tokens。你的模型有單次可處理的 token 限制（GPT-5.2 為 400,000，包括最多 272,000 輸入 tokens 與 128,000 輸出 tokens）。理解 tokens 有助管理對話長短與費用。

## 記憶如何運作

聊天記憶解決了無狀態問題，維持對話歷史。發送請求前，框架會加上相關以往訊息。當你問「我的名字是什麼？」，系統實際上會送出完整歷史讓模型看到你之前說過「我的名字是 John」。

LangChain4j 提供自動處理此事的記憶實作。你決定保留多少訊息，框架管理上下文窗口。下圖展示 MessageWindowChatMemory 如何維持一個滑動窗口，保留最近訊息。

<img src="../../../translated_images/zh-HK/memory-window.bbe67f597eadabb3.webp" alt="記憶窗口概念" width="800"/>

*MessageWindowChatMemory 維持最近訊息的滑動窗口，自動丟棄舊訊息*

## 本模組如何使用 LangChain4j

本模組在快速入門基礎上，整合 Spring Boot 並加入對話記憶。以下是各部分如何組合：

**依賴** — 添加兩個 LangChain4j 庫：

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

**聊天模型** — 將 Azure OpenAI 設定為 Spring Bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java))：

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

Builder 從 `azd up` 設定的環境變數讀取憑證。設定 `baseUrl` 為你的 Azure 終端點，讓 OpenAI 用戶端支援 Azure OpenAI。

**對話記憶** — 使用 MessageWindowChatMemory 追蹤聊天歷史 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

用 `withMaxMessages(10)` 建立記憶，保留最舊 10 則訊息。用型別包裝器添加用戶和 AI 訊息：`UserMessage.from(text)` 和 `AiMessage.from(text)`。用 `memory.messages()` 取得歷史，送到模型。服務按對話 ID 維護不同記憶實例，允許多用戶同時聊天。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) 並詢問：
> - 「MessageWindowChatMemory 滿了時怎麼決定丟棄哪些訊息？」
> - 「可不可以用資料庫代替記憶體中實作，打造自訂記憶存儲？」
> - 「我要如何加入摘要功能來壓縮舊的對話內容？」

無狀態聊天端點完全不使用記憶 — 只是像快速入門一樣呼叫 `chatModel.chat(prompt)`。有狀態端點則是加入訊息到記憶，擷取歷史並與每次請求一起送出。模型配置相同，模式不同。

## 部署 Azure OpenAI 基礎設施

**Bash:**
```bash
cd 01-introduction
azd up  # 選擇訂閱和位置（建議使用 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 選擇訂閱及地點（建議使用 eastus2）
```

> **注意：** 若出現逾時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），只需重新執行 `azd up` 即可。Azure 資源可能還在背景佈建中，重試可待資源達終態時完成部署。

此命令將會：
1. 部署帶有 GPT-5.2 和 text-embedding-3-small 模型的 Azure OpenAI 資源
2. 自動在專案根目錄建立 `.env` 憑證檔案
3. 設定所有必需的環境變數

**部署遇到問題？** 請查看 [Infrastructure README](infra/README.md)，內容含副域名衝突、手動 Azure Portal 部署步驟及模型配置指引。

**確認部署成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 會自動產生 `.env` 檔。若後續需要更新，可手動編輯 `.env` 或重新執行：
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

**驗證部署：**

確保根目錄有 `.env` 文件且內含 Azure 憑證。從模組目錄 (`01-introduction/`) 執行：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**方法一：使用 Spring Boot 儀表板（推薦 VS Code 使用者）**

開發容器包含 Spring Boot 儀表板擴充套件，提供視覺介面管理所有 Spring Boot 應用。你可在 VS Code 左側活動列找到（尋找 Spring Boot 圖示）。

從 Spring Boot 儀表板可以：
- 查看工作區所有 Spring Boot 應用程式
- 一鍵啟動/停止應用
- 實時檢視應用日誌
- 監控應用狀態

只需點擊 "introduction" 旁的播放按鈕啟動本模組，或同時啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot 儀表板" width="400"/>

*VS Code 的 Spring Boot 儀表板 — 一處管理全部模組啟動、停止與監控*

**方法二：使用 shell 腳本**

啟動全部網頁應用（模組 01-04）：

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

兩個腳本均會自動從根目錄 `.env` 載入環境變數，並於無 JAR 檔時自動編譯。

> **注意：** 若想先手動編譯全部模組，再啟動：
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

**停止應用：**

**Bash:**
```bash
./stop.sh  # 只限此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只有此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

應用提供網頁介面，左、右兩邊實現不同聊天模式。

<img src="../../../translated_images/zh-HK/home-screen.121a03206ab910c0.webp" alt="應用程式主畫面" width="800"/>

*面板同時顯示簡單聊天（無狀態）與對話聊天（有狀態）選項*

### 無狀態聊天（左側面板）

先試試看。問「我的名字是 John」，接著立刻問「我的名字是什麼？」。模型不會記得，因為每則訊息獨立。這展示了基本語言模型整合的核心問題 — 無對話上下文。

<img src="../../../translated_images/zh-HK/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="無狀態聊天示範" width="800"/>

*AI 不會記得上一則訊息中的名字*

### 有狀態聊天（右側面板）

在這裡試試同樣的順序。說「我的名字是 John」，接著問「我的名字是什麼？」這次它會記得。差異就在 MessageWindowChatMemory — 它會維持對話歷史並附加到每次請求。這就是生產對話式 AI 的工作方式。

<img src="../../../translated_images/zh-HK/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="有狀態聊天示範" width="800"/>

*AI 會記得之前對話裡你的名字*

兩個面板使用相同 GPT-5.2 模型，唯一差別是記憶。這清楚表明記憶對應用程式的價值及其必要性。

## 後續步驟

**下一模組：** [02-prompt-engineering - GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導覽：** [← 上一章：Module 00 - 快速入門](../00-quick-start/README.md) | [回主頁](../README.md) | [下一章：Module 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件經由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於提供準確的翻譯，但請注意自動翻譯可能包含錯誤或不準確之處。原文件以其母語版本為權威來源。如涉及重要資訊，建議尋求專業人工翻譯。我們不對因使用此翻譯而產生的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->