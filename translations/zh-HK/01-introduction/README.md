# 模組 01：開始使用 LangChain4j

## 目錄

- [你將學到什麼](../../../01-introduction)
- [先決條件](../../../01-introduction)
- [理解核心問題](../../../01-introduction)
- [理解 Tokens](../../../01-introduction)
- [記憶的運作原理](../../../01-introduction)
- [這如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基礎設施](../../../01-introduction)
- [本地執行應用程式](../../../01-introduction)
- [使用應用程式](../../../01-introduction)
  - [無狀態聊天（左側面板）](../../../01-introduction)
  - [有狀態聊天（右側面板）](../../../01-introduction)
- [下一步](../../../01-introduction)

## 你將學到什麼

如果你完成了快速開始，就會看到如何發送提示並獲取回答。這是基礎，但真正的應用需要更多。本模組教你如何建立能記住上下文並維持狀態的對話式 AI — 這是一次性示範和生產就緒應用之間的差異。

我們將在整個指南中使用 Azure OpenAI 的 GPT-5.2，因其先進的推理能力讓不同模式的行為更明顯。當你加入記憶時，就能清楚看到差異，讓你更容易理解每個元件帶給應用程式的價值。

你將建立一個示範兩種模式的應用：

**無狀態聊天** — 每個請求獨立，模型不記得先前訊息。這是你在快速開始中使用的模式。

**有狀態對話** — 每個請求都包含對話歷史，模型能跨多輪保持上下文。這是生產應用所需要的。

## 先決條件

- 具 Azure OpenAI 存取權的 Azure 訂閱
- Java 21、Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** Java、Maven、Azure CLI 及 Azure Developer CLI (azd) 已預先安裝於提供的開發容器中。

> **注意：** 本模組使用 Azure OpenAI 上的 GPT-5.2。部署會透過 `azd up` 自動設定，請勿修改程式碼中的模型名稱。

## 理解核心問題

語言模型是無狀態的。每次 API 呼叫都是獨立的。如果你先說「我叫 John」，然後問「我叫什麼名字？」，模型不會知道你剛介紹過自己。它每次都當這是你第一次對話。

這對簡單的問答還可以，但對真正的應用沒用。客服機器人需要記住你告訴它的資訊。個人助理需要上下文。任何多輪對話都需要記憶。

<img src="../../../translated_images/zh-HK/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*無狀態（獨立呼叫）與有狀態（上下文感知）對話的差異*

## 理解 Tokens

在深入對話前，理解 tokens 很重要 — 它是語言模型處理文本的基本單位：

<img src="../../../translated_images/zh-HK/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*文本拆解成 tokens 的範例 — 「I love AI!」分成 4 個獨立處理單元*

Tokens 是 AI 模型度量和處理文本的方式。單詞、標點，甚至空白都可以是 token。你模型有同時處理的 token 數量限制（GPT-5.2 是 400,000，包含最多 272,000 輸入 tokens 和 128,000 輸出 tokens）。理解 tokens 幫助你管理對話長度與成本。

## 記憶的運作原理

聊天記憶透過維持對話歷史解決了無狀態問題。在把請求送給模型前，框架會加上相關的先前訊息。當你問「我叫什麼名字？」時，系統其實送出整段對話歷史，讓模型知道你之前說過「我叫 John」。

LangChain4j 提供自動處理的記憶實作。你選擇保留多少訊息，框架管理上下文視窗。

<img src="../../../translated_images/zh-HK/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 維持最近訊息的滑動視窗，自動刪除較舊的訊息*

## 這如何使用 LangChain4j

本模組在快速開始基礎上擴充，整合 Spring Boot 並加入對話記憶。元件組合如下：

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

**聊天模型** — 將 Azure OpenAI 設定為 Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java))：

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

建構器會從由 `azd up` 設定的環境變數讀取憑證。設定 `baseUrl` 為你的 Azure 端點，OpenAI 客戶端便能與 Azure OpenAI 配合使用。

**對話記憶** — 使用 MessageWindowChatMemory 跟蹤聊天記錄 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

以 `withMaxMessages(10)` 建立記憶，保留最近 10 則訊息。用 `UserMessage.from(text)` 和 `AiMessage.from(text)` 封裝用戶和 AI 訊息。用 `memory.messages()` 取得歷史並送給模型。服務對每個對話 ID 存有獨立記憶，允許多位用戶同時聊天。

> **🤖 可用 [GitHub Copilot](https://github.com/features/copilot) Chat 嘗試：** 打開 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)，問：
> - 「MessageWindowChatMemory 在視窗滿時，如何決定要刪除哪些訊息？」
> - 「我可以用資料庫實作自訂的記憶儲存而非記憶體中嗎？」
> - 「怎麼加入摘要功能以壓縮舊對話歷史？」

無狀態聊天端點完全不使用記憶，直接呼叫 `chatModel.chat(prompt)`，跟快速開始一樣。有狀態端點則加訊息到記憶，取得歷史並把上下文加到每次請求。模型配置相同，模式不同。

## 部署 Azure OpenAI 基礎設施

**Bash:**
```bash
cd 01-introduction
azd up  # 選擇訂閱及地點（建議使用 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 選擇訂閱及地點（建議使用 eastus2）
```

> **注意：** 如果遇到逾時錯誤（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），只要重新執行 `azd up`。Azure 資源可能仍在佈建中，重試可等待資源達到終止狀態後完成部署。

此程序會：
1. 部署 Azure OpenAI 資源，包含 GPT-5.2 和 text-embedding-3-small 模型
2. 自動在專案根目錄產生包含憑證的 `.env` 檔
3. 設定所有所需的環境變數

**部署有問題？** 請參閱 [Infrastructure README](infra/README.md)，內有包含子網域名稱衝突、Azure Portal 手動部署步驟與模型配置指引等詳細故障排除。

**驗證部署成功：**

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 命令會自動生成 `.env` 檔。若日後需更新，可編輯 `.env` 手動或重新生成：
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

確認根目錄存在包含 Azure 憑證的 `.env` 檔案：

**Bash:**
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

**方案 1：使用 Spring Boot Dashboard（建議 VS Code 使用者）**

開發容器已包含 Spring Boot Dashboard 擴充套件，提供視覺介面管理所有 Spring Boot 應用。你可在 VS Code 左側的活動列找到（尋找 Spring Boot 圖示）。

透過 Spring Boot Dashboard，你可以：
- 查看工作區中所有可用 Spring Boot 應用
- 點擊即可啟動／停止應用
- 實時查看應用日誌
- 監控應用狀態

點擊 "introduction" 旁的播放鈕啟動本模組，或一次啟動全部模組。

<img src="../../../translated_images/zh-HK/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**方案 2：使用 shell 腳本**

啟動所有 Web 應用（模組 01-04）：

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

兩者腳本會自動從根目錄 `.env` 載入環境變數，且若 JAR 檔案不存在會自動編譯。

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

在瀏覽器開啟 http://localhost:8080 。

**停止應用：**

**Bash:**
```bash
./stop.sh  # 此模組僅限
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

應用程式提供並排的兩種聊天實作的網頁介面。

<img src="../../../translated_images/zh-HK/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*儀表板顯示簡易聊天（無狀態）與對話式聊天（有狀態）選項*

### 無狀態聊天（左側面板）

先試這個。說「我叫 John」，接著立刻問「我叫什麼名字？」模型不會記得，因為每則訊息都是獨立的。這展示了基本語言模型整合的核心問題 — 沒有對話上下文。

<img src="../../../translated_images/zh-HK/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不會記得你之前說的名字*

### 有狀態聊天（右側面板）

現在在這裡試同樣的對話。說「我叫 John」，再問「我叫什麼名字？」這次可以記得。差別在於 MessageWindowChatMemory — 它維持對話歷史，並在每次請求附上這些上下文。這就是生產對話式 AI 的方式。

<img src="../../../translated_images/zh-HK/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 會記得早先對話中的名字*

兩側面板使用相同的 GPT-5.2 模型。唯一差別是是否使用記憶。這清楚展示記憶對應用程式的貢獻及其在實際使用中的重要性。

## 下一步

**下一模組：** [02-prompt-engineering - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**導航：** [← 上一章：模組 00 - 快速開始](../00-quick-start/README.md) | [回主頁](../README.md) | [下一章：模組 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原文文件以其原始語言版本為權威依據。對於關鍵資訊，建議採用專業人工翻譯。我們對因使用本翻譯而引起的任何誤解或錯誤詮釋概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->