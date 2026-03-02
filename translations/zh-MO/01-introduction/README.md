# Module 01: 開始使用 LangChain4j

## 目錄

- [影片導覽](../../../01-introduction)
- [你將學習到的內容](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [了解核心問題](../../../01-introduction)
- [了解 Tokens](../../../01-introduction)
- [記憶如何運作](../../../01-introduction)
- [如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎設施](../../../01-introduction)
- [本地執行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [下一步](../../../01-introduction)

## 影片導覽

觀看此現場會議，說明如何開始使用本模組：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 你將學習到的內容

在快速入門中，你使用 GitHub 模型來發送提示、呼叫工具、建立 RAG 管道，並測試防護措施。那些示範展現了可能性 — 現在我們轉到 Azure OpenAI 與 GPT-5.2，開始打造生產風格的應用程式。本模組專注於記憶對話上下文與狀態維持的會話式 AI — 這正是快速入門示範在背後使用但未解釋的概念。

整個指南將使用 Azure OpenAI 的 GPT-5.2，因其先進的推理能力令不同模式的行為更明顯。當你加入記憶時，差異一目了然。這使得理解每個組件為應用程式帶來什麼更容易。

你將構建一個示範兩種模式的應用程式：

**無狀態聊天** - 每個請求獨立。模型不記得先前訊息。這是快速入門時使用的模式。

**有狀態對話** - 每個請求包含對話歷史。模型跨多輪維持上下文。這是生產環境應用程式所需。

## 先決條件

- 具 Azure OpenAI 存取權限的 Azure 訂閱
- Java 21，Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的開發容器中已預裝 Java、Maven、Azure CLI 與 Azure Developer CLI (azd)。

> **注意：** 本模組使用 Azure OpenAI 的 GPT-5.2。部署會透過 `azd up` 自動設定 — 請勿修改程式碼中的模型名稱。

## 了解核心問題

語言模型是無狀態的。每次 API 呼叫都是獨立的。如果你先傳「我的名字是 John」然後問「我的名字是什麼？」，模型不知道你剛自我介紹過。它將每個請求視為你第一次對話。

這對簡單的問答沒問題，但對真實應用程式無用。客服機器人需要記住你告訴它的事。個人助理需要上下文。任何多輪對話都需要記憶。

下圖對比兩種方法 — 左邊是會忘記你名字的無狀態呼叫；右邊是有 ChatMemory 記憶機制的有狀態呼叫。

<img src="../../../translated_images/zh-MO/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*無狀態（獨立呼叫）和有狀態（具上下文意識）對話的差異*

## 了解 Tokens

深入對話前，先了解 tokens — 語言模型處理的基本文字單位：

<img src="../../../translated_images/zh-MO/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*文字如何被切分為 tokens 範例 - "I love AI!" 變成 4 個分離處理單元*

Tokens 是 AI 模型衡量與處理文字的方式。單字、標點甚至空白都可以是 token。您的模型有限制同時處理多少 tokens（GPT-5.2 是 400,000 個，包含最多 272,000 輸入 tokens 和 128,000 輸出 tokens）。了解 tokens 有助於管理對話長度與成本。

## 記憶如何運作

聊天記憶解決無狀態問題，透過維持對話歷史。在送出請求給模型前，框架會將相關先前訊息置於前端。當你問「我的名字是什麼？」時，系統實際發送整段對話歷史，讓模型看到你之前說過「我的名字是 John」。

LangChain4j 提供的記憶實作會自動處理這一切。你決定保留多少訊息，框架管理上下文視窗。下圖顯示 MessageWindowChatMemory 如何維持最近訊息的滑動窗口。

<img src="../../../translated_images/zh-MO/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 維持最近訊息的滑動窗口，自動丟棄舊訊息*

## 如何使用 LangChain4j

本模組在快速入門基礎上，整合 Spring Boot 並加入會話記憶。組件如下：

**相依性** — 增加兩個 LangChain4j 函式庫：

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

建構器從 `azd up` 設定的環境變數讀取憑證。設定 `baseUrl` 為您的 Azure 端點，使 OpenAI 用戶端能與 Azure OpenAI 配合。

**會話記憶** — 用 MessageWindowChatMemory 追蹤聊天歷史 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

用 `withMaxMessages(10)` 建立只保留最近 10 則訊息的記憶。使用經型別封裝的 `UserMessage.from(text)` 和 `AiMessage.from(text)` 加入用戶與 AI 訊息。用 `memory.messages()` 取得歷史並送給模型。服務對每個對話 ID 儲存分開的記憶實例，允許多用戶同時聊天。

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 打開 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)，並詢問：
> - "MessageWindowChatMemory 在視窗滿了時如何決定丟棄哪些訊息？"
> - "我可以用資料庫實作自訂記憶儲存，而非採用記憶體嗎？"
> - "如何新增摘要功能，以壓縮舊的對話歷史？"

無狀態聊天端點完全不使用記憶 — 就是 `chatModel.chat(prompt)`，與快速入門相同。有狀態端點則將訊息加到記憶中，撈歷史，並隨每次請求附加上下文。相同模型配置，不同模式。

## 部署 Azure OpenAI 基礎設施

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

> **注意：** 若遇到逾時錯誤 (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`)，只要重新執行 `azd up` 即可。Azure 資源可能仍在背景部署中，重試操作可讓部署在資源進入終態後完成。

此操作將：
1. 部署含 GPT-5.2 和 text-embedding-3-small 模型的 Azure OpenAI 資源
2. 自動於專案根目錄生成包含憑證的 `.env` 檔案
3. 設定所有必要的環境變數

**部署遇到問題？** 請參閱 [基礎設施 README](infra/README.md) 詳細故障排除，包括子域名衝突、手動 Azure 门户部署流程及模型設定指南。

**驗證部署是否成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 指令會自動生成 `.env` 檔。如需更新，您可手動編輯 `.env` 檔或重新生成：
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

## 本地執行應用程式

**驗證部署：**

確保根目錄有 `.env` 檔並正確設置 Azure 憑證。於模組目錄 (`01-introduction/`) 執行：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**選項 1：使用 Spring Boot Dashboard（建議 VS Code 使用者）**

開發容器已包含 Spring Boot Dashboard 擴充套件，提供視覺化介面管理所有 Spring Boot 應用。請於 VS Code 左側活動欄尋找 Spring Boot 圖示。

透過 Spring Boot Dashboard，你能：
- 看到工作區內所有 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時查看應用程式日誌
- 監控應用狀態

點擊「introduction」旁播放按鈕開始此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot Dashboard — 從一處啟動、停止並監控所有模組*

**選項 2：使用 shell 指令腳本**

啟動全部網頁應用（模組 01-04）：

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

或只啟動此模組：

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

兩者腳本皆自動從根目錄 `.env` 檔載入環境變數，若 JAR 不存在則建置。

> **注意：** 若你偏好先手動建置所有模組再啟動：
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

**停止應用：**

**Bash:**
```bash
./stop.sh  # 僅此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

應用程式提供網頁介面，並排展示兩種聊天實作。

<img src="../../../translated_images/zh-MO/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*儀表板展示簡易聊天（無狀態）與會話聊天（有狀態）選項*

### 無狀態聊天（左側面板）

先試試這個。輸入「我的名字是 John」，接著立刻問「我的名字是什麼？」模型不會記得，因為每則訊息都是獨立的。這展示了基本語言模型整合的核心問題 — 沒有對話上下文。

<img src="../../../translated_images/zh-MO/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不會記得你前一則訊息中的名字*

### 有狀態聊天（右側面板）

現在在這裡試同樣序列。輸入「我的名字是 John」，再問「我的名字是什麼？」這次它會記得。原因在於 MessageWindowChatMemory — 它維持對話歷史並隨每次請求包含之。這就是生產級會話式 AI 的作法。

<img src="../../../translated_images/zh-MO/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 記得對話早先說過你的名字*

兩側皆使用相同 GPT-5.2 模型。唯一差異是有無記憶。這清楚展現了記憶為應用帶來的價值，以及它為什麼對真正使用場景至關重要。

## 下一步

**下一模組：** [02-prompt-engineering - 用 GPT-5.2 進行提示工程](../02-prompt-engineering/README.md)

---

**導覽：** [← 上一課程：Module 00 - 快速入門](../00-quick-start/README.md) | [回主頁](../README.md) | [下一課程：Module 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。文件的原始語言版本應被視為權威來源。對於重要資訊，建議使用專業人工翻譯。本公司不對因使用本翻譯而引起的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->