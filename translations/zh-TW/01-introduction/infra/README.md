# LangChain4j Azure 基礎架構入門

## 目錄

- [先決條件](../../../../01-introduction/infra)
- [架構](../../../../01-introduction/infra)
- [建立的資源](../../../../01-introduction/infra)
- [快速開始](../../../../01-introduction/infra)
- [設定](../../../../01-introduction/infra)
- [管理命令](../../../../01-introduction/infra)
- [成本優化](../../../../01-introduction/infra)
- [監控](../../../../01-introduction/infra)
- [故障排除](../../../../01-introduction/infra)
- [更新基礎架構](../../../../01-introduction/infra)
- [清理](../../../../01-introduction/infra)
- [檔案結構](../../../../01-introduction/infra)
- [安全性建議](../../../../01-introduction/infra)
- [其他資源](../../../../01-introduction/infra)

此目錄包含透過 Bicep 與 Azure Developer CLI (azd) 為部署 Azure OpenAI 資源所撰寫的 Azure 基礎架構即程式碼（IaC）。

## 先決條件

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)（版本 2.50.0 或更高）
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd)（版本 1.5.0 或更高）
- 具有建立資源權限的 Azure 訂閱

## 架構

**簡化的本地開發設定** - 僅部署 Azure OpenAI，所有應用程式在本機執行。

基礎架構會部署以下 Azure 資源：

### AI 服務
- **Azure OpenAI**：具兩個模型部署的認知服務：
  - **gpt-5.2**：聊天完成模型（20K TPM 容量）
  - **text-embedding-3-small**：用於 RAG 的嵌入模型（20K TPM 容量）

### 本地開發
所有 Spring Boot 應用程式在您的機器上本地執行：
- 01-introduction（埠號 8080）
- 02-prompt-engineering（埠號 8083）
- 03-rag（埠號 8081）
- 04-tools（埠號 8084）

## 建立的資源

| 資源類型 | 資源名稱樣式 | 用途 |
|--------------|----------------------|---------|
| 資源群組 | `rg-{environmentName}` | 包含所有資源 |
| Azure OpenAI | `aoai-{resourceToken}` | AI 模型主機 |

> **注意：**`{resourceToken}` 是根據訂閱 ID、環境名稱及位置所產生的唯一字串

## 快速開始

### 1. 部署 Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up
```

**PowerShell:**
```powershell
cd 01-introduction
azd up
```

當提示時：
- 選擇您的 Azure 訂閱
- 選擇位置（建議：`eastus2`，以取得 GPT-5.2 支援）
- 確認環境名稱（預設：`langchain4j-dev`）

此操作會建立：
- 含 GPT-5.2 與 text-embedding-3-small 的 Azure OpenAI 資源
- 輸出連線詳細資訊

### 2. 取得連線詳細資訊

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

此操作會顯示：
- `AZURE_OPENAI_ENDPOINT`：您的 Azure OpenAI 端點 URL
- `AZURE_OPENAI_KEY`：驗證用 API 金鑰
- `AZURE_OPENAI_DEPLOYMENT`：聊天模型名稱（gpt-5.2）
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`：嵌入模型名稱

### 3. 本地執行應用程式

`azd up` 命令將自動在根目錄建立 `.env` 檔案，包含所有必要的環境變數。

**建議**：啟動所有網頁應用程式：

**Bash:**
```bash
# 從根目錄開始
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# 從根目錄開始
cd ../..
.\start-all.ps1
```

或啟動單一模組：

**Bash:**
```bash
# 範例：僅啟動介紹模組
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# 範例：只啟動介紹模組
cd ../01-introduction
.\start.ps1
```

這兩個腳本會自動從 `azd up` 建立的根目錄 `.env` 檔案載入環境變數。

## 設定

### 自訂模型部署

若要更改模型部署，請編輯 `infra/main.bicep` 並修改 `openAiDeployments` 參數：

```bicep
param openAiDeployments array = [
  {
    name: 'gpt-5.2'  // Model deployment name
    model: {
      format: 'OpenAI'
      name: 'gpt-5.2'
      version: '2025-12-11'  // Model version
    }
    sku: {
      name: 'GlobalStandard'
      capacity: 20  // TPM in thousands
    }
  }
  // Add more deployments...
]
```

可用模型與版本：https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### 變更 Azure 區域

若要在不同區域部署，請編輯 `infra/main.bicep`：

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

檢查 GPT-5.2 可用區域：https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

若對 Bicep 檔案做出更動後要更新基礎架構：

**Bash:**
```bash
# 重新建構 ARM 模板
az bicep build --file infra/main.bicep

# 預覽變更
azd provision --preview

# 套用變更
azd provision
```

**PowerShell:**
```powershell
# 重建 ARM 範本
az bicep build --file infra/main.bicep

# 預覽變更
azd provision --preview

# 套用變更
azd provision
```

## 清理

刪除所有資源：

**Bash:**
```bash
# 刪除所有資源
azd down

# 刪除所有內容包括環境
azd down --purge
```

**PowerShell:**
```powershell
# 刪除所有資源
azd down

# 刪除所有內容，包括環境
azd down --purge
```

**警告**：此操作會永久刪除所有 Azure 資源。

## 檔案結構

## 成本優化

### 開發/測試
針對開發/測試環境，可降低成本：
- Azure OpenAI 使用標準層（S0）
- 在 `infra/core/ai/cognitiveservices.bicep` 設定較低容量（10K TPM，非 20K）
- 不使用時刪除資源：`azd down`

### 生產
針對生產環境：
- 根據使用量提升 OpenAI 容量（50K+ TPM）
- 啟用區域冗餘以提高可用性
- 實施適當監控與成本警示

### 成本估算
- Azure OpenAI：依據標記數（輸入+輸出）付費
- GPT-5.2：約每百萬標記 $3-5 美元（請確認最新價格）
- text-embedding-3-small：約每百萬標記 $0.02 美元

價格計算器：https://azure.microsoft.com/pricing/calculator/

## 監控

### 查看 Azure OpenAI 指標

前往 Azure 入口網站 → 您的 OpenAI 資源 → 指標：
- 以標記為基礎的使用率
- HTTP 請求率
- 響應時間
- 活動標記數

## 故障排除

### 問題：Azure OpenAI 子網域名稱衝突

**錯誤訊息：**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**原因：**
根據您的訂閱/環境產生的子網域名稱已被使用，可能是先前部署未完全清除所致。

**解決方案：**
1. **方案一 - 使用不同環境名稱：**
   
   **Bash:**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell:**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **方案二 - 透過 Azure 入口網站手動部署：**
   - 前往 Azure 入口網站 → 建立資源 → Azure OpenAI
   - 選擇資源的唯一名稱
   - 部署以下模型：
     - **GPT-5.2**
     - **text-embedding-3-small**（用於 RAG 模組）
   - **重要：** 記錄您的部署名稱，必須與 `.env` 配置相符
   - 部署完成後，從「金鑰與端點」獲取端點與 API 金鑰
   - 在專案根目錄建立 `.env` 檔案，內容如下：
     
     **範例 `.env` 檔案：**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**模型部署命名指南：**
- 使用簡單且一致名稱：`gpt-5.2`、`gpt-4o`、`text-embedding-3-small`
- 部署名稱必須與 `.env` 中設定完全相符
- 常見錯誤：建立的模型名稱與程式碼中引用的名稱不符

### 問題：所選區域無 GPT-5.2

**解決方案：**
- 選擇有 GPT-5.2 支援的區域（例如 eastus2）
- 檢查可用性：https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### 問題：部署配額不足

**解決方案：**
1. 至 Azure 入口網站申請配額提升
2. 或在 `main.bicep` 使用較低容量（例如 capacity: 10）

### 問題：本地執行時「找不到資源」

**解決方案：**
1. 確認部署狀態：`azd env get-values`
2. 檢查端點與金鑰是否正確
3. 確認資源群組確實存在於 Azure 入口網站

### 問題：驗證失敗

**解決方案：**
- 確認 `AZURE_OPENAI_API_KEY` 設定正確
- 金鑰格式應為 32 字元十六進位字串
- 需要時從 Azure 入口網站取得新金鑰

### 部署失敗

**問題**：`azd provision` 因配額或容量錯誤失敗

**解決方案**： 
1. 嘗試不同區域，請參閱 [變更 Azure 區域](../../../../01-introduction/infra) 章節了解配置方式
2. 確認訂閱具備 Azure OpenAI 配額：
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### 應用程式無法連線

**問題**：Java 應用程式顯示連線錯誤

**解決方案**：
1. 驗證環境變數是否匯出：
   
   **Bash:**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell:**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. 檢查端點格式是否正確（應為 `https://xxx.openai.azure.com`）
3. 確認 API 金鑰是 Azure 入口網站上的主要或次要金鑰

**問題**：收到 401 未授權來自 Azure OpenAI

**解決方案**：
1. 從 Azure 入口網站「金鑰與端點」取得新 API 金鑰
2. 重新匯出 `AZURE_OPENAI_API_KEY` 環境變數
3. 確認模型部署已完成（檢查 Azure 入口網站）

### 效能問題

**問題**：響應時間緩慢

**解決方案**：
1. 檢查 Azure 入口網站上的 OpenAI 標記使用率及限制
2. 若達容量上限，提升 TPM 容量
3. 考慮使用較高的推理工作量等級（低／中／高）

## 更新基礎架構

```
infra/
├── main.bicep                       # Main infrastructure definition
├── main.json                        # Compiled ARM template (auto-generated)
├── main.bicepparam                  # Parameter file
├── README.md                        # This file
└── core/
    └── ai/
        └── cognitiveservices.bicep  # Azure OpenAI module
```

## 安全性建議

1. **切勿提交 API 金鑰** - 使用環境變數管理
2. **本地使用 .env 檔案** - 將 `.env` 加入 `.gitignore`
3. **定期輪替金鑰** - 在 Azure 入口網站產生新金鑰
4. **限制存取權限** - 使用 Azure RBAC 控制誰能存取資源
5. **監控使用情形** - 在 Azure 入口網站設定成本警示

## 其他資源

- [Azure OpenAI 服務文件](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 模型文件](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI 文件](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep 文件](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI 官方整合](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## 支援

如有問題：
1. 先查看上述 [故障排除章節](../../../../01-introduction/infra)
2. 檢查 Azure 入口網站上的 Azure OpenAI 服務健康狀態
3. 於本專案倉庫開啟 issue

## 授權

相關細節請參考根目錄 [LICENSE](../../../../LICENSE) 檔案。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。儘管我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用此翻譯而導致的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->