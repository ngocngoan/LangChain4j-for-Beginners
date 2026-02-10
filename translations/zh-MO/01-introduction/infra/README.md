# LangChain4j Azure 基建入門指南

## 目錄

- [前置條件](../../../../01-introduction/infra)
- [架構](../../../../01-introduction/infra)
- [建立的資源](../../../../01-introduction/infra)
- [快速開始](../../../../01-introduction/infra)
- [設定](../../../../01-introduction/infra)
- [管理指令](../../../../01-introduction/infra)
- [成本優化](../../../../01-introduction/infra)
- [監控](../../../../01-introduction/infra)
- [故障排除](../../../../01-introduction/infra)
- [更新基建](../../../../01-introduction/infra)
- [清理](../../../../01-introduction/infra)
- [檔案結構](../../../../01-introduction/infra)
- [安全建議](../../../../01-introduction/infra)
- [其他資源](../../../../01-introduction/infra)

此目錄包含使用 Bicep 與 Azure Developer CLI (azd) 以代碼形式描述的 Azure 基礎設施，專為部署 Azure OpenAI 資源。

## 前置條件

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)（版本 2.50.0 或以上）
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd)（版本 1.5.0 或以上）
- 擁有創建資源權限的 Azure 訂閱

## 架構

**簡化的本地開發環境設置** – 僅部署 Azure OpenAI，所有應用本地運行。

此基礎設施會部署以下 Azure 資源：

### AI 服務
- **Azure OpenAI**：認知服務，包含兩個模型部署：
  - **gpt-5.2**：聊天完成模型（容量 20K TPM）
  - **text-embedding-3-small**：用於 RAG 的嵌入模型（容量 20K TPM）

### 本地開發
所有 Spring Boot 應用均在您機器本地運行：
- 01-introduction（端口 8080）
- 02-prompt-engineering（端口 8083）
- 03-rag（端口 8081）
- 04-tools（端口 8084）

## 建立的資源

| 資源類型 | 資源名稱模式 | 用途 |
|--------------|----------------------|---------|
| 資源群組 | `rg-{environmentName}` | 包含所有資源 |
| Azure OpenAI | `aoai-{resourceToken}` | AI 模型主機 |

> **注意：** `{resourceToken}` 是從訂閱 ID、環境名稱及位置組合生成的唯一字串

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

提示時：
- 選擇您的 Azure 訂閱
- 選擇地區（建議：`eastus2` 支援 GPT-5.2）
- 確認環境名稱（預設：`langchain4j-dev`）

此操作將建立：
- 含 GPT-5.2 與 text-embedding-3-small 的 Azure OpenAI 資源
- 輸出連線詳情

### 2. 取得連線詳情

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

### 3. 本地運行應用程式

`azd up` 指令會在根目錄自動建立 `.env` 檔案，包含所有必要環境變數。

**建議**：啟動所有網頁應用：

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

或啟動單個模組：

**Bash:**
```bash
# 範例：只啟動介紹模組
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

若要更改模型部署，請編輯 `infra/main.bicep`，並修改 `openAiDeployments` 參數：

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

可用模型及版本說明：https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### 更改 Azure 區域

如需在其他區域部署，請編輯 `infra/main.bicep`：

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

檢查 GPT-5.2 可用性：https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

變更 Bicep 檔案後要更新基建：

**Bash:**
```bash
# 重建 ARM 範本
az bicep build --file infra/main.bicep

# 預覽更改
azd provision --preview

# 套用更改
azd provision
```

**PowerShell:**
```powershell
# 重建 ARM 模板
az bicep build --file infra/main.bicep

# 預覽更改
azd provision --preview

# 應用更改
azd provision
```


## 清理

刪除所有資源：

**Bash:**
```bash
# 刪除所有資源
azd down

# 刪除包括環境在內的一切
azd down --purge
```

**PowerShell:**
```powershell
# 刪除所有資源
azd down

# 刪除包括環境在內的一切
azd down --purge
```

**警告**：此操作會永久刪除所有 Azure 資源。

## 檔案結構

## 成本優化

### 開發/測試環境
對於開發或測試環境，可降低成本：
- Azure OpenAI 使用 Standard 階層 (S0)
- 在 `infra/core/ai/cognitiveservices.bicep` 設定較低容量（10K TPM 替代 20K）
- 非使用時刪除資源：`azd down`

### 產品環境
生產環境：
- 根據使用量提升 OpenAI 容量（50K TPM 以上）
- 啟用區域冗餘提高穩定性
- 實施妥善監控與成本警示

### 成本估算
- Azure OpenAI：按使用的 token（輸入與輸出）付費
- GPT-5.2：約每百萬 token 3-5 美元（請查詢最新價格）
- text-embedding-3-small：約每百萬 token 0.02 美元

價格計算器：https://azure.microsoft.com/pricing/calculator/

## 監控

### 檢視 Azure OpenAI 指標

前往 Azure 入口網站 → 你的 OpenAI 資源 → 指標：
- 以 token 計算的使用率
- HTTP 請求速率
- 回應時間
- 活躍 token 數量

## 故障排除

### 問題：Azure OpenAI 子網域名稱衝突

**錯誤訊息：**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**原因：**
您的訂閱/環境產生的子網域名稱已被使用，可能是之前部署未完全清除。

**解決方案：**
1. **方案一 - 使用不同的環境名稱：**

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
   - 為資源選擇唯一名稱
   - 部署以下模型：
     - **GPT-5.2**
     - **text-embedding-3-small**（用於 RAG 模組）
   - **重要：** 記錄部署名稱，必須與 `.env` 設定相符
   - 部署完成後，於「金鑰與端點」取得端點及 API 金鑰
   - 在專案根目錄建立 `.env` 檔案，內容示例如下：

     **範例 `.env` 檔案：**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**模型部署命名指南：**
- 使用簡單且一致的命名：`gpt-5.2`、`gpt-4o`、`text-embedding-3-small`
- 部署名稱需完全與 `.env` 中設定相符
- 常見錯誤：建立模型名稱與程式碼引用名稱不一致

### 問題：所選區域無 GPT-5.2

**解決方案：**
- 選擇有 GPT-5.2 存取權的區域（例如 eastus2）
- 查詢可用性：https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### 問題：部署配額不足

**解決方案：**
1. 在 Azure 入口網站申請配額提升
2. 或在 `main.bicep` 中使用較低容量 (例如 capacity: 10)

### 問題：本地運行時出現「找不到資源」

**解決方案：**
1. 驗證部署狀態：`azd env get-values`
2. 確認端點與金鑰正確
3. 確認資源群組是否存在於 Azure 入口網站

### 問題：驗證失敗

**解決方案：**
- 確認 `AZURE_OPENAI_API_KEY` 已正確設定
- 金鑰格式需為 32 字元的十六進位字串
- 如有需要，從 Azure 入口網站重新取得金鑰

### 部署失敗

**問題**：`azd provision` 指令因配額或容量錯誤失敗

**解決方案**：
1. 嘗試其他區域，詳見[更改 Azure 區域](../../../../01-introduction/infra)
2. 確認您的訂閱是否已有 Azure OpenAI 配額：

   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```


### 應用程式無法連線

**問題**：Java 應用出現連線錯誤

**解決方案**：
1. 確認已匯出環境變數：

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

2. 確認端點格式正確（應為 `https://xxx.openai.azure.com`）
3. 確認 API 金鑰是 Azure 入口網站的主要或次要金鑰

**問題**：Azure OpenAI 回傳 401 未授權

**解決方案**：
1. 從 Azure 入口網站→「金鑰與端點」取得新金鑰
2. 重新匯出 `AZURE_OPENAI_API_KEY` 環境變數
3. 確認模型部署已完成（可於 Azure 入口網站檢查）

### 效能問題

**問題**：回應時間緩慢

**解決方案**：
1. 在 Azure 入口網站指標檢查 OpenAI token 使用及限制情況
2. 若達限制，提升 TPM 容量
3. 考慮調整推理努力等級（低/中/高）

## 更新基礎設施

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

1. **切勿提交 API 金鑰至版本控制系統**，採用環境變數管理
2. **本地使用 .env 檔案**，並將 `.env` 加入 `.gitignore`
3. **定期輪替金鑰**，於 Azure 入口網站產生新金鑰
4. **限制存取權限**，使用 Azure RBAC 控制資源存取
5. **監控使用量**，於 Azure 入口網站設定成本警示

## 其他資源

- [Azure OpenAI 服務文件](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 模型文件](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI 文件](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep 文件](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI 官方整合](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## 支援

遇到問題時：
1. 查看上方的 [故障排除](../../../../01-introduction/infra) 部分
2. 檢查 Azure OpenAI 服務健康狀態於 Azure 入口網站
3. 在此倉庫開啟 issue

## 授權

請參閱根目錄的 [LICENSE](../../../../LICENSE) 檔案了解詳情。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯結果可能包含錯誤或不準確之處。原始文件以其本地語言版本為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而引致之任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->