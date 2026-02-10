# LangChain4j 的 Azure 基礎架構快速入門

## 目錄

- [先決條件](../../../../01-introduction/infra)
- [架構](../../../../01-introduction/infra)
- [已建立資源](../../../../01-introduction/infra)
- [快速開始](../../../../01-introduction/infra)
- [設定](../../../../01-introduction/infra)
- [管理指令](../../../../01-introduction/infra)
- [成本優化](../../../../01-introduction/infra)
- [監控](../../../../01-introduction/infra)
- [疑難排解](../../../../01-introduction/infra)
- [更新基礎架構](../../../../01-introduction/infra)
- [清理](../../../../01-introduction/infra)
- [檔案結構](../../../../01-introduction/infra)
- [安全建議](../../../../01-introduction/infra)
- [其他資源](../../../../01-introduction/infra)

此目錄包含使用 Bicep 及 Azure Developer CLI (azd) 的 Azure 程式碼基礎架構 (IaC)，用於部署 Azure OpenAI 資源。

## 先決條件

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)（版本 2.50.0 或更高）
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd)（版本 1.5.0 或更高）
- 具有建立資源權限的 Azure 訂閱

## 架構

**簡化的本地開發設定** - 僅部署 Azure OpenAI，所有應用程式本地執行。

基礎架構部署以下 Azure 資源：

### AI 服務
- **Azure OpenAI**：具備兩個模型部署的認知服務：
  - **gpt-5.2**：聊天完成模型（20K TPM 容量）
  - **text-embedding-3-small**：用於 RAG 的嵌入模型（20K TPM 容量）

### 本地開發
所有 Spring Boot 應用程式皆在您本機執行：
- 01-introduction（埠號 8080）
- 02-prompt-engineering（埠號 8083）
- 03-rag（埠號 8081）
- 04-tools（埠號 8084）

## 已建立資源

| 資源類型  | 資源名稱模式            | 用途         |
|-----------|-------------------------|--------------|
| 資源群組  | `rg-{environmentName}`  | 包含所有資源 |
| Azure OpenAI | `aoai-{resourceToken}` | AI 模型託管  |

> **注意：** `{resourceToken}` 是由訂閱 ID、環境名稱和位置生成的唯一字串

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

於提示時：
- 選擇您的 Azure 訂閱
- 選擇地區（建議：`eastus2`，以取得 GPT-5.2 支援）
- 確認環境名稱（預設為 `langchain4j-dev`）

這將創建：
- 含 GPT-5.2 和 text-embedding-3-small 的 Azure OpenAI 資源
- 輸出連線詳細資料

### 2. 取得連線詳細資料

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

此命令會顯示：
- `AZURE_OPENAI_ENDPOINT`：您的 Azure OpenAI 端點 URL
- `AZURE_OPENAI_KEY`：用於驗證的 API 金鑰
- `AZURE_OPENAI_DEPLOYMENT`：聊天模型名稱（gpt-5.2）
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`：嵌入模型名稱

### 3. 本地執行應用程式

`azd up` 指令會自動在根目錄建立 `.env` 檔案，包含所有必要的環境變數。

**建議**：啟動所有 Web 應用程式：

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
# 例子：只啟動介紹模組
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# 範例：只啟動介紹模組
cd ../01-introduction
.\start.ps1
```

兩個腳本皆會自動從 `azd up` 建立的根目錄 `.env` 檔案載入環境變數。

## 設定

### 自訂模型部署

要變更模型部署，請編輯 `infra/main.bicep` 並修改 `openAiDeployments` 參數：

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

可用的模型及版本：https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### 更改 Azure 區域

若要在不同區域部署，請編輯 `infra/main.bicep`：

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

查看 GPT-5.2 可用性：https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

修改 Bicep 文件後更新基礎架構：

**Bash:**
```bash
# 重建 ARM 模板
az bicep build --file infra/main.bicep

# 預覽更改
azd provision --preview

# 套用更改
azd provision
```

**PowerShell:**
```powershell
# 重新構建 ARM 模板
az bicep build --file infra/main.bicep

# 預覽更改
azd provision --preview

# 套用更改
azd provision
```

## 清理

刪除所有資源：

**Bash:**
```bash
# 刪除所有資源
azd down

# 刪除包括環境在內的所有內容
azd down --purge
```

**PowerShell:**
```powershell
# 刪除所有資源
azd down

# 刪除所有內容，包括環境
azd down --purge
```

**警告**：此動作將永久刪除所有 Azure 資源。

## 檔案結構

## 成本優化

### 開發/測試
對於開發/測試環境，您可以降低成本：
- 使用標準等級（S0）Azure OpenAI
- 在 `infra/core/ai/cognitiveservices.bicep` 將容量設定為較低的值（10K TPM 而非 20K）
- 不使用時刪除資源：`azd down`

### 生產環境
生產環境：
- 根據使用量提高 OpenAI 容量（50K+ TPM）
- 啟用可用性區域冗餘以提供更高可用性
- 實作適當監控及成本警示

### 成本估算
- Azure OpenAI 以 Token 付費（輸入 + 輸出）
- GPT-5.2：約每 100 萬個 Token 3-5 美元（請查詢最新價格）
- text-embedding-3-small：約每 100 萬個 Token 0.02 美元

價格計算器：https://azure.microsoft.com/pricing/calculator/

## 監控

### 查看 Azure OpenAI 指標

前往 Azure 入口網站 → 您的 OpenAI 資源 → 指標：
- Token 基礎使用率
- HTTP 請求率
- 回應時間
- 活動 Token

## 疑難排解

### 問題：Azure OpenAI 子網域名稱衝突

**錯誤訊息：**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**原因：**
由您的訂閱/環境生成的子網域名稱已被使用，可能因先前部署未完全清除。

**解決方案：**
1. **方案 1 - 使用不同的環境名稱：**
   
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

2. **方案 2 - 透過 Azure 入口網站手動部署：**
   - 前往 Azure 入口網站 → 建立資源 → Azure OpenAI
   - 選擇唯一的資源名稱
   - 部署以下模型：
     - **GPT-5.2**
     - **text-embedding-3-small**（供 RAG 模組使用）
   - **重要：** 記錄部署名稱，必須與 `.env` 設定相符
   - 部署完成後，從「金鑰與端點」取得端點和 API 金鑰
   - 在專案根目錄建立 `.env` 檔案，內容如下：
     
     **範例 `.env` 檔案：**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**模型部署命名準則：**
- 使用簡單且一致的名稱：`gpt-5.2`、`gpt-4o`、`text-embedding-3-small`
- 部署名稱必須與 `.env` 配置完全一致
- 常見錯誤：建立模型名稱與程式碼中使用的名稱不符

### 問題：所選區域無 GPT-5.2

**解決方案：**
- 選擇有 GPT-5.2 的區域（例如 eastus2）
- 查詢可用性：https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### 問題：部署配額不足

**解決方案：**
1. 在 Azure 入口網站提交配額提升申請
2. 或在 `main.bicep` 中使用較低容量（例如：capacity: 10）

### 問題：本地執行出現「找不到資源」

**解決方案：**
1. 確認部署：`azd env get-values`
2. 檢查端點及金鑰是否正確
3. 確保資源群組存在於 Azure 入口網站

### 問題：驗證失敗

**解決方案：**
- 確認 `AZURE_OPENAI_API_KEY` 是否正確設定
- 金鑰格式應為 32 字元十六進位字串
- 如有需要，從 Azure 入口網站取得新金鑰

### 部署失敗

**問題**：`azd provision` 因配額或容量錯誤而失敗

**解決方案**： 
1. 嘗試使用不同區域—參閱 [更改 Azure 區域](../../../../01-introduction/infra) 章節設定區域
2. 確認您的訂閱有 Azure OpenAI 配額：
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### 應用程式無法連接

**問題**：Java 應用程式顯示連線錯誤

**解決方案**：
1. 確認環境變數已正確導出：
   
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
3. 確認 API 金鑰為 Azure 入口網站主金鑰或次金鑰

**問題**：從 Azure OpenAI 收到 401 未授權回應

**解決方案**：
1. 從 Azure 入口網站 → 金鑰與端點取得新的 API 金鑰
2. 重新導出 `AZURE_OPENAI_API_KEY` 環境變數
3. 確認模型部署已完成（在 Azure 入口網站檢查）

### 效能問題

**問題**：回應速度緩慢

**解決方案**：
1. 查看 Azure 入口網站中 OpenAI 的 Token 使用量及限制
2. 如達到限制，增加 TPM 容量
3. 考慮使用較高的推理效力層級（低/中/高）

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

## 安全建議

1. **切勿提交 API 金鑰** — 使用環境變數管理
2. **本地使用 .env 檔案** — 並將 `.env` 加入 `.gitignore`
3. **定期輪替金鑰** — 於 Azure 入口網站產生新的金鑰
4. **限制存取權限** — 使用 Azure RBAC 管控資源存取
5. **監控使用狀況** — 在 Azure 入口網站設定成本警示

## 其他資源

- [Azure OpenAI 服務文件](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 模型文件](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI 文件](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep 文件](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI 官方整合](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## 支援

有問題時：
1. 檢查上方的 [疑難排解](../../../../01-introduction/infra) 章節
2. 查看 Azure OpenAI 服務在 Azure 入口網站的健康狀態
3. 在本倉庫開立議題

## 授權

詳見根目錄的 [LICENSE](../../../../LICENSE) 檔案。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件乃使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於翻譯的準確性，但請注意，自動翻譯可能存在錯誤或不準確之處。文件的原文版本應被視為權威來源。對於重要資訊，建議諮詢專業人類翻譯。我們對因使用此翻譯而產生的任何誤解或誤譯概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->