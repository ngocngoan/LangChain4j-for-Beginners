# LangChain4j ಪ್ರಾರಂಭಿಸಲು Azure ಮೂಲಸೌಕರ್ಯ

## ವಿಷಯ ವೊಂದು

- [ಆವಶ್ಯಕತೆಗಳು](../../../../01-introduction/infra)
- [ವಾಸ್ತುಶಿಲ್ಪ](../../../../01-introduction/infra)
- [ನಿರ್ಮಿಸಲಾದ ಸಂಪನ್ಮೂಲಗಳು](../../../../01-introduction/infra)
- [ತ್ವರಿತ ಪ್ರಾರಂಭ](../../../../01-introduction/infra)
- [ರೂಪರೇಖೆ](../../../../01-introduction/infra)
- [ನಿರ್ವಹಣಾ ಆದೇಶಗಳು](../../../../01-introduction/infra)
- [ವೆಚ್ಚ ಮಿತಿಗೊಳಿಸುವಿಕೆ](../../../../01-introduction/infra)
- [ನಿರೀಕ್ಷಣಾ](../../../../01-introduction/infra)
- [ಸamas್ಯಾ ಪರಿಹಾರ](../../../../01-introduction/infra)
- [ಮೂಲಸೌಕರ್ಯ ನವೀಕರಿಸುವಿಕೆ](../../../../01-introduction/infra)
- [ಶುದ್ಧೀಕರಣ](../../../../01-introduction/infra)
- [ಫೈಲ್ ಸಂರಚನೆ](../../../../01-introduction/infra)
- [ಸುರಕ್ಷತಾ ಶಿಫಾರಸುಗಳು](../../../../01-introduction/infra)
- [ಹೆಚ್ಚುವರಿ ಸಂಪನ್ಮೂಲಗಳು](../../../../01-introduction/infra)

ಈ ಡೈರೆಕ್ಟರಿಯಲ್ಲಿ Bicep ಮತ್ತು Azure Developer CLI (azd) ಬಳಸಿ ಆಜೂರ್ ಮೂಲಸೌಕರ್ಯವನ್ನು ಕೋಡ್ (IaC) ರೂಪದಲ್ಲಿ ಮತ್ತು ಆಜೂರ್ OpenAI ಸಂಪನ್ಮೂಲಗಳನ್ನು ನಿಯೋಜಿಸಲು ಹೊಂದಿಸಲಾಗಿದೆ.

## ಆವಶ್ಯಕತೆಗಳು

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (ಆವೃತಿಯ 2.50.0 ಅಥವಾ ನಂತರದ)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (ಆವೃತಿಯ 1.5.0 ಅಥವಾ ನಂತರದ)
- ಸಂಪನ್ಮೂಲಗಳನ್ನು ರಚಿಸಲು ಅನುಮತಿ ಹೊಂದಿದ ಆಜೂರ್ ಚಂದಾದಾರಿಕೆ

## ವಾಸ್ತುಶಿಲ್ಪ

**ಸರಳೀಕೃತ ಸ್ಥಳೀಯ ಅಭಿವೃದ್ಧಿ ವ್ಯವಸ್ಥೆ** - ಕೇವಲ ಆಜೂರ್ OpenAI ನಿಯೋಜಿಸಿ, ಎಲ್ಲಾ ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ನಿಮ್ಮ ಸ್ಥಳೀಯ ಯಂತ್ರದಲ್ಲಿ ಚಾಲನೆ ಮಾಡು.

ಮೂಲಸೌಕರ್ಯ ಕೆಳಕಂಡ ಆಜೂರ್ ಸಂಪನ್ಮೂಲಗಳನ್ನು ನಿಯೋಜಿಸುತ್ತದೆ:

### AI ಸೇವೆಗಳು
- **Azure OpenAI**: ಎರಡು ಮಾದರಿ ನಿಯೋಜನೆಗಳೊಂದಿಗೆ ಕೊರ್ಟಿವ್ ಸೇವೆಗಳು:
  - **gpt-5.2**: ಮಾತೃಚರಿತ್ರೆಯ ಪೂರ್ಣಗೊಳಿಸುವಿಕೆ ಮಾದರಿ (20K TPM ಸಾಮರ್ಥ್ಯ)
  - **text-embedding-3-small**: RAG ಮುಖ್ಯಕ್ಕಾಗಿ ಇಂಬೆಡ್ಡಿಂಗ್ ಮಾದರಿ (20K TPM ಸಾಮರ್ಥ್ಯ)

### ಸ್ಥಳೀಯ ಅಭಿವೃದ್ಧಿ
ಎಲ್ಲಾ ಸ್ಪ್ರಿಂಗ್ ಬೂಟ್ ಅಪ್ಲಿಕೇಶನ್ ಗಳು ನಿಮ್ಮ ಯಂತ್ರದಲ್ಲಿ ಸ್ಥಳೀಯವಾಗಿ ನಡೆಯುತ್ತವೆ:
- 01-introduction (ಪೋರ್ಟ್ 8080)
- 02-prompt-engineering (ಪೋರ್ಟ್ 8083)
- 03-rag (ಪೋರ್ಟ್ 8081)
- 04-tools (ಪೋರ್ಟ್ 8084)

## ನಿರ್ಮಿಸಲಾದ ಸಂಪನ್ಮೂಲಗಳು

| ಸಂಪನ್ಮೂಲ ಪ್ರಕಾರ | ಸಂಪನ್ಮೂಲ ಹೆಸರು ಮಾದರಿ | ಉದ್ದೇಶ |
|--------------|----------------------|---------|
| ಸಂಪನ್ಮೂಲ ಗುೂಪು | `rg-{environmentName}` | ಎಲ್ಲಾ ಸಂಪನ್ಮೂಲಗಳನ್ನು ಒಳಗೊಂಡಿದೆ |
| ಆಜೂರ್ OpenAI | `aoai-{resourceToken}` | AI ಮಾದರಿ ನಿರ್ವಹಣೆ |

> **ಗಮನಿಸಿ**: `{resourceToken}`是一串唯一字符串，由订阅 ID、环境名称和位置生成。

## ತ್ವರಿತ ಪ್ರಾರಂಭ

### 1. ಆಜೂರ್ OpenAI ನಿಯೋಜಿಸಿ

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

ಪ್ರಾಂಪ್ಟ್ ಆಗುವಾಗ:
- ನಿಮ್ಮ ಆಜೂರ್ ಚಂದಾದಾರಿಕೆಯನ್ನು ಆಯ್ಕೆ ಮಾಡಿ
- ಸ್ಥಳವನ್ನು ಆಯ್ಕೆಮಾಡಿ ( ಶಿಫಾರಸು: GPT-5.2 ಲಭ್ಯತೆಗೆ ಪುರ್ವಾರ್ಧ `eastus2`)
- ಪರಿಸರ ಹೆಸರನ್ನು ದೃಢೀಕರಿಸಿ (ಪೂರ್ವನಿಯೋಜಿತ: `langchain4j-dev`)

ಇದು ನಿರ್ಮಿಸುವುದು:
- GPT-5.2 ಮತ್ತು text-embedding-3-small ಹೊಂದಿರುವ ಆಜೂರ್ OpenAI ಸಂಪನ್ಮೂಲ
- ಸಂಪರ್ಕ ವಿವರಗಳನ್ನು ಔಟ್‌ಪುಟ್ ಮಾಡು

### 2. ಸಂಪರ್ಕ ವಿವರಗಳನ್ನು ಪಡೆಯಿರಿ

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

ಇದರಲ್ಲಿ ತೋರಿಸಲಾಗುತ್ತದೆ:
- `AZURE_OPENAI_ENDPOINT`: ನಿಮ್ಮ ಆಜೂರ್ OpenAI ಎಂಡ್ಪಾಯಿಂಟ್ URL
- `AZURE_OPENAI_KEY`: ದೃಢೀಕರಣಕ್ಕಾಗಿ API ಕೀ
- `AZURE_OPENAI_DEPLOYMENT`: ಚಾಟ್ ಮಾದರಿ ಹೆಸರು (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: ಇಂಬೆಡ್ಡಿಂಗ್ ಮಾದರಿ ಹೆಸರು

### 3. ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಸ್ಥಳೀಯವಾಗಿ ಓಡಿಸಿ

`azd up` ಆದೇಶವು ಅವಶ್ಯಕ ಪರಿಸರ ಚರಗಳೊಂದಿಗೆ ಮೂಲ ಡೈರೆಕ್ಟರಿಯಲ್ಲಿ `.env` ಫೈಲ್ ಅನ್ನು ತಾನೇ ಸೃಷ್ಟಿಸುತ್ತದೆ.

**ಶಿಫಾರಸು ಇಲ್ಲಿದೆ:** ಎಲ್ಲಾ ವೆಬ್ ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಪ್ರಾರಂಭಿಸಿ:

**Bash:**
```bash
# ಮರ মূল ಡೈರೆಕ್ಟರಿಯಿಂದ
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# ಮೂಲ ಡೈರೆಕ್ಟರಿ ನಿಂದ
cd ../..
.\start-all.ps1
```

ಅಥವಾ ಒಬ್ಬೊಂದೇ ಒಂದು ಮೋಡ್‌ಯೂಲ್ ಪ್ರಾರಂಭಿಸಿ:

**Bash:**
```bash
# ಉದಾಹರಣೆ: ಪ್ರವೇಶ ಘಟಕವನ್ನು ಮಾತ್ರ ಪ್ರಾರಂಭಿಸಿ
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# ಉದಾಹರಣೆ: ಪರಿಚಯ ಮಾಯಾಜಾಲವನ್ನು ಮಾತ್ರ ಪ್ರಾರಂಭಿಸಿ
cd ../01-introduction
.\start.ps1
```

ಈ ಎರಡು ಸ್ಕ್ರಿಪ್ಟ್‌ಗಳು `azd up` ಮೂಲಕ ಸೃಷ್ಟಿಸಲಾದ ಮೂಲ `.env` ಫೈಲ್‌ನಿಂದ ಪರಿಸರ ಚರಗಳನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಲೋಡ್ ಮಾಡುತ್ತವೆ.

## ರೂಪರೇಖೆ

### ಮಾದರಿ ನಿಯೋಜನೆ ಆಯ್ಕೆ ಮಾಡುವುದು

ಮಾದರಿ ನಿಯೋಜನೆಗಳನ್ನು ಬದಲಾಯಿಸಲು, `infra/main.bicep` ಅನ್ನು ಬದಲಿಸಿ ಮತ್ತು `openAiDeployments` ಪರಿಮಾಣವನ್ನು ತಿದ್ದುಪಡಿಸಿ:

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

ಲಭ್ಯವಿರುವ ಮಾದರಿಗಳು ಮತ್ತು ಆವೃತ್ತಿಗಳು: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### ಆಜೂರ್ ಪ್ರಾಂತ್ಯಗಳನ್ನು ಬದಲಾಯಿಸುವುದು

ಇತರೆ ಪ್ರಾಂತ್ಯದಲ್ಲಿ ನಿಯೋಜಿಸಲು, `infra/main.bicep` ತಿದ್ದುಪಡಿಸಿರಿ:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 ಲಭ್ಯತೆ ಪರಿಶೀಲಿಸಿ: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep ಕಡತಗಳ ಬದಲಾವಣೆಗಳ ನಂತರ ಮೂಲಸೌಕರ್ಯ ನವೀಕರಿಸಲು:

**Bash:**
```bash
# ARM ಟೆಂಪ್ಲೇಟನ್ನು ಮರುನirmಾಣ ಮಾಡಿರಿ
az bicep build --file infra/main.bicep

# ಬದಲಾವಣೆಗಳನ್ನು ಪೂರ್ವಾವಲೋಕನ ಮಾಡಿ
azd provision --preview

# ಬದಲಾವಣೆಗಳನ್ನು ಜಾರಿಗೊಳಿಸಿ
azd provision
```

**PowerShell:**
```powershell
# ARM ಟೆಂಪ್ಲೆಟ್ ಅನ್ನು ಮರುನirmaïಸು
az bicep build --file infra/main.bicep

# ಬದಲಾವಣೆಗಳನ್ನು ಪೂರ್ಣ դիտಿ
azd provision --preview

# ಬದಲಾವಣೆಗಳನ್ನು ಅನ್ವಯಿಸಿ
azd provision
```

## ಶುದ್ಧೀಕರಣ

ಎಲ್ಲಾ ಸಂಪನ್ಮೂಲಗಳನ್ನು ಅಳಿಸಲು:

**Bash:**
```bash
# ಎಲ್ಲಾ ಸಂಪನ್ಮೂಲಗಳನ್ನು ಅಳಿಸಿ
azd down

# ಪರಿಸರ ಸೇರಿದಂತೆ ಎಲ್ಲವನ್ನು ಅಳಿಸಿ
azd down --purge
```

**PowerShell:**
```powershell
# ಎಲ್ಲಾ ಸಂಪನ್ಮೂಲಗಳನ್ನು ಅಳಿಸಿ
azd down

# ಪರಿಸರವನ್ನು ಒಳಗೊಂಡು ಎಲ್ಲವನ್ನೂ ಅಳಿಸಿ
azd down --purge
```

**ಜಾಗೃತಿ**: ಇದು ಎಲ್ಲಾ ಆಜೂರ್ ಸಂಪನ್ಮೂಲಗಳನ್ನು ಶಾಶ್ವತವಾಗಿ ಅಳಿಸುತ್ತದೆ.

## ಫೈಲ್ ಸಂರಚನೆ

## ವೆಚ್ಚ ಮಿತಿಗೊಳಿಸುವಿಕೆ

### ಅಭಿವೃದ್ಧಿ/ಪರೀಕ್ಷೆ
ಊರಿನ/ಪರೀಕ್ಷಾ ಪರಿಸರಗಳಿಗೆ ಖರ್ಚು ಕಡಿಮೆಯಾಗಿಸಲು:
- ಆಜೂರ್ OpenAIಗೆ ಸ್ಟ್ಯಾಂಡರ್ಡ್ ತಹಗು (S0) ಉಪಯೋಗಿಸಿ
- `infra/core/ai/cognitiveservices.bicep` ನಲ್ಲಿ ಕಡಿಮೆ ಸಾಮರ್ಥ್ಯವನ್ನು ನಿಗದಿಸು (20K ಬದಲು 10K TPM)
- ಬಳಸದಾಗ ಸಂಪನ್ಮೂಲಗಳನ್ನು ಅಳಿಸಿ: `azd down`

### ಉತ್ಪಾದನೆ
ಉತ್ಪಾದನೆಗೆ:
- ಬಳಕೆಯನ್ನು ಆಧರಿಸಿ OpenAI ಸಾಮರ್ಥ್ಯವನ್ನು ಹೆಚ್ಚಿಸಿ (50K+ TPM)
- ಹೆಚ್ಚಿನ ಲಭ್ಯತೆಗಾಗಿ ಝೋನ್ ಪೂರ್ಣಪ್ರತಿರೂಪಿ ಸಕ್ರಿಯಗೊಳಿಸಿ
- ಸರಿಯಾದ ನಿಗಾ ಮತ್ತು ವೆಚ್ಚ ಸೂಚನೆಗಳು ಜಾರಿಗೊಳಿಸಿ

### ವೆಚ್ಚ ಅಂದಾಜು
- ಆಜೂರ್ OpenAI: ಪ್ರತಿ ಟೋಕನ್ ಆದಾಯ (ಇನ್‌ಪುಟ್ + ಔಟ್‌ಪುಟ್)
- GPT-5.2: ಸುಮಾರು $3-5 ಪ್ರತಿ 1M ಟೋಕನ್ಗಳು (ಪ್ರಸ್ತುತ ಬೆಲೆ ಪರಿಶೀಲಿಸಿ)
- text-embedding-3-small: ಸುಮಾರು $0.02 ಪ್ರತಿ 1M ಟೋಕನ್ಗಳು

ಬೆಲೆ ಲೆಕ್ಕಾಚಾರ: https://azure.microsoft.com/pricing/calculator/

## ನಿರೀಕ್ಷಣಾ

### ಆಜೂರ್ OpenAI ಮೆಟ್ರಿಕ್ಸ್ ನೋಡಲು

ಆಜೂರ್ ಪೋರ್ಟಲ್ → ನಿಮ್ಮ OpenAI ಸಂಪನ್ಮೂಲ → ಮೆಟ್ರಿಕ್ಸ್:
- ಟೋಕನ್ ಆಧಾರಿತ ಬಳಕೆ
- HTTP ವಿನಂತಿ ದರ
- ಪ್ರತಿಕ್ರಿಯೆ ಸಮಯ
- ಸಕ್ರಿಯ ಟೋಕನ್ಗಳು

## ಸಮಸ್ಯೆ ಪರಿಹಾರ

### ಸಮಸ್ಯೆ: ಆಜೂರ್ OpenAI ಉಪಡೊಮೇನ್ ಹೆಸರು ಮರುಕುರುಳು

**ದೋಷ ಸಂದೇಶ:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**ಕಾರಣ:**
ನಿಮ್ಮ ಚಂದಾದಾರಿಕೆ/ಪರಿಸರದಿಂದ ರಚಿಸಲಾದ ಉಪಡೊಮೇನ್ ಹೆಸರು ಈಗಾಗಲೇ ಬಳಸಲಾಗುತ್ತಿದೆ, ಬಹುಶಃ ಹಳೆಯ ನಿಯೋಜನೆಯಿಂದ ಸಂಪೂರ್ಣವಾಗಿ ಅಳಿಸದಿರುವದು.

**ಪರಿಹಾರ:**
1. **ಆಯ್ಕೆ 1 - ವಿಭಿನ್ನ ಪರಿಸರ ಹೆಸರನ್ನು ಬಳಸಿ:**  
   
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

2. **ಆಯ್ಕೆ 2 - Azure ಪೋರ್ಟಲ್ ಮೂಲಕ ಕೈಯಿಂದ ನಿಯೋಜನೆ:**
   - ಆಜೂರ್ ಪೋರ್ಟಲ್‌ಗೆ ಹೋಗಿ → ಸಂಪನ್ಮೂಲವನ್ನು ರಚಿಸಿ → ಆಜೂರ್ OpenAI ಆಯ್ಕೆಮಾಡಿ
   - ನಿಮ್ಮ ಸಂಪನ್ಮೂಲಕ್ಕೆ ಅನನ್ಯ ಹೆಸರು ಕೊಡಿ
   - ಕೆಳಕಂಡ ಮಾದರಿಗಳನ್ನು ನಿಯೋಜಿಸಿ:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG ಮೋಡ್ಯೂಲ್‌ಗಳಿಗೆ)
   - **ಮುಖ್ಯ:** ನಿಮ್ಮ ನಿಯೋಜನೆ ಹೆಸರುಗಳು `.env` ರೂಪರೇಖೆಯೊಂದಿಗೆ ಹೊಂದಿಕೊಳ್ಳಬೇಕು
   - ನಿಯೋಜನೆಯ ನಂತರ, "ಕೀಲಿಗಳು ಮತ್ತು ಎಂಡ್ಪಾಯಿಂಟ್" ನಲ್ಲಿ ನಿಮ್ಮ ಎಂಡ್ಪಾಯಿಂಟ್ ಮತ್ತು API ಕೀ ಪಡೆಯಿರಿ
   - ಯೋಜನೆಯ ಮೂಲದಲ್ಲಿ `.env` ಫೈಲ್ ರಚಿಸಿ:

     **ಉದಾಹರಣೆಯ `.env` ಫೈಲ್:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**ಮಾದರಿ ನಿಯೋಜನೆ ಹೆಸರು ಮಾರ್ಗಸೂಚಿಗಳು:**
- ಸರಳ, ಸತತ ಹೆಸರುಗಳನ್ನು ಬಳಸಿ: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- ನಿಯೋಜನೆ ಹೆಸರುಗಳು `.env` ನಲ್ಲಿ ನಿಯೋಜಿಸಿದಂತೆ ನಿಖರವಾಗಿರಬೇಕು
- ಸಾಮಾನ್ಯ ದೋಷ: ಒಂದೇ ಮಾದರಿಗೆ ಬೇರೆ ಹೆಸರಿನಿಂದ ಬಳಸುವುದು

### ಸಮಸ್ಯೆ: GPT-5.2 ಆಯ್ಕೆ ಮಾಡಿದ ಪ್ರಾಂತ್ಯದಲ್ಲಿ ಲಭ್ಯವಿಲ್ಲ

**ಪರಿಹಾರ:**
- GPT-5.2 ಪ್ರವೇಶ ಇರುವ ಪ್ರಾಂತ್ಯವನ್ನು ಆಯ್ಕೆಮಾಡಿ (ಉದಾ: eastus2)
- ಲಭ್ಯತೆ ಪರಿಶೀಲಿಸಿ: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### ಸಮಸ್ಯೆ: ನಿಯೋಜನೆಗೆ ತಗಲುವ ಹಂಚಿಕೆ ಮಿತಿ ಕಡಿಮೆ

**ಪರಿಹಾರ:**
1. ಆಜೂರ್ ಪೋರ್ಟಲ್‌ನಲ್ಲಿ ಹಂಚಿಕೆ ಮಿತಿಯನ್ನು ಹೆಚ್ಚಿಸಲು ವಿನಂತಿಸಿ
2. ಅಥವಾ `main.bicep` ನಲ್ಲಿ ಕಡಿಮೆ ಸಾಮರ್ಥ್ಯ ಬಳಸಿ (ಉದಾ: capacity: 10)

### ಸಮಸ್ಯೆ: ಸ್ಥಳೀಯವಾಗಿ ಚಾಲನೆ ವೇಳೆ "ಸಂಪನ್ಮೂಲ ಕಂಡುಬಂದಿಲ್ಲ" ದೋಷ

**ಪರಿಹಾರ:**
1. ನಿಯೋಜನೆ ಪರಿಶೀಲಿಸಿ: `azd env get-values`
2. ಎಂಡ್ಪಾಯಿಂಟ್ ಮತ್ತು ಕೀ ಸರಿಯಾದುದೇ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ
3. ಆಜೂರ್ ಪೋರ್ಟಲ್‌ನಲ್ಲಿ ಸಂಪನ್ಮೂಲ ಗುಂಪು ಅಸ್ತಿತ್ವದಲ್ಲಿದೆಯೇ ತಪಾಸಿಸು

### ಸಮಸ್ಯೆ: ದೃಢೀಕರಣ ವಿಫಲ

**ಪರಿಹಾರ:**
- `AZURE_OPENAI_API_KEY` ಸರಿಯಾಗಿ ಸೆಟ್ ಆಗಿದೆಯೇ ಅಂತ ಪರಿಶೀಲಿಸಿ
- ಕೀ 32-ಅಕ್ಷರ ഹെಕ್ಸಾಡೆಸಿಮಲ್ ಸ್ಟ್ರಿಂಗ್ ಆಗಿರಬೇಕು
- ಅಗತ್ಯವಿದ್ದರೆ ಆಜೂರ್ ಪೋರ್ಟಲ್‌ನಿಂದ ಹೊಸ ಕೀ ಪಡೆಯಿರಿ

### ನಿಯೋಜನೆ ವಿಫಲವಾಗುತ್ತದೆ

**ಸಮಸ್ಯೆ**: `azd provision` ನಲ್ಲಿ ಹಂಚಿಕೆ ಅಥವಾ ಸಾಮರ್ಥ್ಯ ದೋಷಗಳು

**ಪರಿಹಾರ**:
1. ವಿಭಿನ್ನ ಪ್ರಾಂತ್ಯವನ್ನು ಪ್ರಯತ್ನಿಸಿ - ಪ್ರಾಂತ್ಯಗಳನ್ನು ರೂಪರೇಖೆಗೊಳಿಸುವ ಬಗ್ಗೆ ನೋಡಲು [ಆಜೂರ್ ಪ್ರಾಂತ್ಯ ಬದಲಾವಣೆ](../../../../01-introduction/infra) ವಿಭಾಗ ನೋಡಿ
2. ನಿಮ್ಮ ಚಂದಾದಾರಿಕೆಗೆ ಆಜೂರ್ OpenAI ಹಂಚಿಕೆ ಲಭ್ಯವಿದೆಯೇ ಎಂದು ಪರಿಶೀಲಿಸಿ:

   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### ಅಪ್ಲಿಕೇಶನ್ ಸಂಪರ್ಕ ಹೊಂದುವುದಿಲ್ಲ

**ಸಮಸ್ಯೆ**: ಜಾವಾ ಅಪ್ಲಿಕೇಶನ್ ಸಂಪರ್ಕ ದೋಷಗಳನ್ನು ತೋರಿಸುತ್ತದೆ

**ಪರಿಹಾರ**:
1. ಪರಿಸರ ಚರಗಳ ರಫ್ತು ಆಗಿದೆಯೇ ಎಂದು ಪರಿಶೀಲಿಸಿ:

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

2. ಎಂಡ್ಪಾಯಿಂಟ್ ಸ್ವರೂಪ ಸರಿಯಾಗಿದೆ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ (`https://xxx.openai.azure.com` होनाದು)
3. API ಕೀ ಆಜೂರ್ ಪೋರ್ಟಲ್‌ನ ಮುಖ್ಯ ಅಥವಾ ಅಧಿಕೃತ ಕೀಲಿಯಾಗಿರಬೇಕು ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ

**ಸಮಸ್ಯೆ**: ಆಜೂರ್ OpenAI ನಿಂದ 401 ಅಜ್ಞಾತ ಮಂಜುರಾತಿ

**ಪರಿಹಾರ**:
1. ಆಜೂರ್ ಪೋರ್ಟಲ್ → ಕೀಲಿಗಳು ಮತ್ತು ಎಂಡ್ಪಾಯಿಂಟ್ ನಲ್ಲಿ ಹೊಸ API ಕೀ ಪಡೆಯಿರಿ
2. `AZURE_OPENAI_API_KEY` ಪರಿಸರ ಚರವನ್ನು ಮರುರೂಪಾಂತರಗೊಳಿಸಿ
3. ಮಾದರಿ ನಿಯೋಜನೆ ಸಮಾಪ್ತಿಯಾಗಿದೆ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ (ಆಜೂರ್ ಪೋರ್ಟಲ್‌ನಲ್ಲಿ ಪರಿಶೀಲನೆ ಮಾಡಿ)

### ಕಾರ್ಯಕ್ಷಮತಾ ಸಮಸ್ಯೆಗಳು

**ಸಮಸ್ಯೆ**: ಸ್ಪಂದನೆ ಸಮಯ ನಿಧಾನವಾಗಿದೆ

**ಪರಿಹಾರ**:
1. ಆಜೂರ್ ಪೋರ್ಟಲ್ ಮೆಟ್ರಿಕ್ಸ್‌ನಲ್ಲಿ OpenAI ಟೋಕನ್ ಬಳಕೆ ಮತ್ತು ಶಾಖ್‌ ಮಾಡಿದ್ದಾರೆ ಎಂದು ಪರಿಶೀಲಿಸಿ
2. ನೀವು ಮಿತಿ ತಲುಪಿದರೆ TPM ಸಾಮರ್ಥ್ಯ ಹೆಚ್ಚಿಸಿ
3. ಹೆಚ್ಚಿನ ನಿರ್ಣಯ-ಪ್ರಯತ್ನ ಮಟ್ಟ (ಕಡಿಮೆ/ಮಧ್ಯಮ/ಹೆಚ್ಚು) ಬಳಸಲು ಪರಿಗಣಿಸಿ

## ಮೂಲಸೌಕರ್ಯ ನವೀಕರಿಸುವಿಕೆ

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

## ಸುರಕ್ಷತಾ ಶಿಫಾರಸುಗಳು

1. **API ಕೀಲಿಗಳನ್ನು ಎಂದಿಗೂ ಕಾಂಮಿಟ್ ಮಾಡಬೇಡಿ** - ಪರಿಸರ ಚರಗಳನ್ನು ಬಳಸಿ
2. **ಸ್ಥಳೀಯವಾಗಿ `.env` ಫೈಲ್‌ಗಳನ್ನು ಬಳಸಿ** - `.env` ಅನ್ನು `.gitignore` ಗೆ ಸೇರಿಸಿ
3. **ಕೀಲಿಗಳನ್ನು ನಿಯತಕಾಲಿಕವಾಗಿ ತಿರುಗಿಸಿ** - ಆಜೂರ್ ಪೋರ್ಟಲ್‌ನಲ್ಲಿ ಹೊಸ ಕೀಗಳನ್ನು ರಚಿಸಿ
4. **ಪ್ರವೇಶವನ್ನು ಮಿತಿಗೊಳಿಸಿ** - ಆಜೂರ್ RBAC ಮೂಲಕ ಯಾರು ಸಂಪನ್ಮೂಲಗಳನ್ನು ಪ್ರವೇಶಿಸಬಹುದು ಎಂದು ನಿಯಂತ್ರಿಸಿ
5. **ಬಳಕೆಯನ್ನು ನಿಗಾ ವಹಿಸಿ** - ಆಜೂರ್ ಪೋರ್ಟಲ್‌ನಲ್ಲಿ ವೆಚ್ಚ ಸೂಚನೆಗಳನ್ನು ಹೊಂದಿಸಿ

## ಹೆಚ್ಚುವರಿ ಸಂಪನ್ಮೂಲಗಳು

- [Azure OpenAI ಸೇವೆ ಪ್ರलेಖನ](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 ಮಾದರಿ ಪ್ರಲೇಖನ](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI ಪ್ರಲೇಖನ](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep ಪ್ರಲೇಖನ](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI ಅಧಿಕೃತ ಸಂಯೋಜನೆ](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## ಸಹಾಯ

ಸಮಸ್ಯೆಗಳಿಗೆ:
1. ಮೇಲಿನ [ಸamas್ಯಾ ಪರಿಹಾರ ವಿಭಾಗವನ್ನು](../../../../01-introduction/infra) ಪರಿಶೀಲಿಸಿ
2. ಆಜೂರ್ ಪೋರ್ಟಲ್‌ನಲ್ಲಿ Azure OpenAI ಸೇವೆಯ ಆರೋಗ್ಯ ಪರಿಶೀಲಿಸಿ
3. ಅಭಿಯಾನದಲ್ಲಿ ಸಮಸ್ಯೆ ತೆರೆಯಿರಿ

## ಪರವಾನಗಿ

ಮೂಲರೇೇಷಿನ [LICENSE](../../../../LICENSE) ಫೈಲ್ ನೋಡಿ.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ಉಪಶಮನ**:  
ಈ ದಸ್ತಾವೇಜನ್ನು AI ಅನುವಾದ ಸೇವೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಬಳಸಿಕೊಂಡು ಅನುವದಿಸಲಾಗಿದೆ. ನಾವು ಸರಿಯಾದ ಅನುವಾದಕ್ಕಾಗಿ ಪ್ರಯತ್ನಿಸುತ್ತಿದ್ದರೂ, ಸ್ವಯಂಚಾಲಿತ ಅನುವಾದಗಳಲ್ಲಿ ದೋಷಗಳು ಅಥವಾ ತಪ್ಪು ಮಾಹಿತಿ ಇರಬಹುದು ಎಂದು ಗಮನಿಸಿ. ಮೂಲ ಭಾಷೆಯಲ್ಲಿನ ಮೂಲ ದಸ್ತಾವೇಜನ್ನು ಅಧಿಕೃತ ಪ್ರಭಾವಶೀಲ ಸ്രೋತೆಯಾಗಿ ಪರಿಗಣಿಸುವುದು ಸೂಚಿತ. ಮುಖ್ಯ ಮಾಹಿತಿಗಾಗಿ ವೃತ್ತಿಪರ ಮಾನವ ಅನುವಾದವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ. ಈ ಅನುವಾದದ ಬಳಕೆಯಿಂದ ಉಂಟಾಗುವ ಯಾವುದೇ ಅರ್ಥಪೂರ್ಣತೆ ಕೊರತೆಯು ಅಥವಾ ತಪ್ಪು ಅರ್ಥಮಾಡಿಕೊಳ್ಳುವುದಕ್ಕಾಗುವ ಜವಾಬ್ದಾರಿಯನ್ನು ನಾವು ಹೊಡೆಯುವುದಿಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->