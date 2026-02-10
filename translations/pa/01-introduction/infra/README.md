# LangChain4j ਲਈ Azure ਇੰਫਰਾਸਟ੍ਰੱਕਚਰ ਸ਼ੁਰੂਆਤ

## ਸੂਚੀ ਸਾਰਣੀ

- [ਜਰੂਰੀਆਂ ਸ਼ਰਤਾਂ](../../../../01-introduction/infra)
- [ਆਰਕੀਟੈਕਚਰ](../../../../01-introduction/infra)
- [ਬਣਾਈਆਂ ਗਈਆਂ ਸਾਧਨ](../../../../01-introduction/infra)
- [ਤੁਰੰਤ ਸ਼ੁਰੂਆਤ](../../../../01-introduction/infra)
- [ਸੰਰਚਨਾ](../../../../01-introduction/infra)
- [ਪ੍ਰਬੰਧਨ ਕਮਾਂਡਾਂ](../../../../01-introduction/infra)
- [ਖਰਚ ਬਚਤ](../../../../01-introduction/infra)
- [ਮਾਨੀਟਰਿੰਗ](../../../../01-introduction/infra)
- [ਸਮੱਸਿਆ ਨਿਵਾਰਣ](../../../../01-introduction/infra)
- [ਇੰਫਰਾਸਟ੍ਰੱਕਚਰ ਅਪਡੇਟ ਕਰਨਾ](../../../../01-introduction/infra)
- [ਸਾਫ਼ ਸਫਾਈ](../../../../01-introduction/infra)
- [ਫਾਇਲ ਸੰਜੋਇਆ](../../../../01-introduction/infra)
- [ਸੁਰੱਖਿਆ ਦੀਆਂ ਸਿਫਾਰਸ਼ਾਂ](../../../../01-introduction/infra)
- [ਵਾਧੂ ਸਾਧਨ](../../../../01-introduction/infra)

ਇਹ ਡਾਇਰੈਕਟਰੀ Bicep ਅਤੇ Azure Developer CLI (azd) ਦੀ ਵਰਤੋਂ ਕਰਕੇ Azure OpenAI ਸੰਸਾਧਨਾਂ ਨੂੰ ਤੈਅ ਕਰਨ ਲਈ ਕੋਡ ਵਜੋਂ Azure ਇੰਫਰਾਸਟ੍ਰੱਕਚਰ (IaC) ਸ਼ਾਮਲ ਕਰਦੀ ਹੈ।

## ਜਰੂਰੀਆਂ ਸ਼ਰਤਾਂ

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (ਵਰਜ਼ਨ 2.50.0 ਜਾਂ ਬਾਅਦ ਵਾਲਾ)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (ਵਰਜ਼ਨ 1.5.0 ਜਾਂ ਬਾਅਦ ਵਾਲਾ)
- ਏਕ Azure ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਜਿਸ ਵਿੱਚ ਸਰੋਤ ਬਣਾਉਣ ਦੀਆਂ ਅਧਿਕਾਰਤਾਂ ਹਨ

## ਆਰਕੀਟੈਕਚਰ

**ਸਰਲਤਮ ਸਥਾਨਕ ਵਿਕਾਸ ਸੈੱਟਅੱਪ** - ਕੇਵਲ Azure OpenAI ਨੂੰ ਤੈਅ ਕਰੋ, ਸਾਰੇ ਐਪਸ ਸਥਾਨਕ ਤੌਰ 'ਤੇ ਚਲਾਓ।

ਇੰਫਰਾਸਟ੍ਰੱਕਚਰ ਹੇਠ ਲਿਖੇ Azure ਸਰੋਤ ਤੈਅ ਕਰਦਾ ਹੈ:

### AI ਸੇਵਾਵਾਂ
- **Azure OpenAI**: ਦੋ ਮਾਡਲ ਤੈਅਕਰਨ ਨਾਲ ਕੌਗਨਿਟਿਵ ਸੇਵਾਵਾਂ:
  - **gpt-5.2**: ਚੈਟ ਕੰਪਲੀਸ਼ਨ ਮਾਡਲ (20K TPM ਸਮਰੱਥਾ)
  - **text-embedding-3-small**: RAG ਲਈ ਐਮਬੈੱਡਿੰਗ ਮਾਡਲ (20K TPM ਸਮਰੱਥਾ)

### ਸਥਾਨਕ ਵਿਕਾਸ
ਸਾਰੇ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨ ਤੁਹਾਡੇ ਮਸ਼ੀਨ ਤੇ ਸਥਾਨਕ ਤੌਰ 'ਤੇ ਚੱਲਦੇ ਹਨ:
- 01-introduction (ਪੋਰਟ 8080)
- 02-prompt-engineering (ਪੋਰਟ 8083)
- 03-rag (ਪੋਰਟ 8081)
- 04-tools (ਪੋਰਟ 8084)

## ਬਣਾਈਆਂ ਗਈਆਂ ਸਰੋਤ

| ਸਰੋਤ ਕਿਸਮ | ਸਰੋਤ ਦਾ ਨਾਮ ਪੈਟਰਨ | ਉਦੇਸ਼ |
|--------------|----------------------|---------|
| ਸਰੋਤ ਗਰੁੱਪ | `rg-{environmentName}` | ਸਾਰੇ ਸਰੋਤ ਸ਼ਾਮਲ |
| Azure OpenAI | `aoai-{resourceToken}` | AI ਮਾਡਲ ਹੋਸਟਿੰਗ |

> **ਨੋਟ:** `{resourceToken}` ਇੱਕ ਵਿਲੱਖਣ ਸਤਰ ਹੈ ਜੋ ਸਬਸਕ੍ਰਿਪਸ਼ਨ ID, ਵਾਤਾਵਰਣ ਨਾਮ ਅਤੇ ਸਥਾਨ ਤੋਂ ਬਣਦਾ ਹੈ

## ਤੁਰੰਤ ਸ਼ੁਰੂਆਤ

### 1. Azure OpenAI ਤੈਅ ਕਰੋ

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

ਜਦੋਂ ਪੁੱਛੇ:
- ਆਪਣੀ Azure ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਚੁਣੋ
- ਇੱਕ ਸਥਾਨ ਚੁਣੋ (ਸਿਫਾਰਸ਼ ਕੀਤੀ: GPT-5.2 ਲਈ `eastus2`)
- ਵਾਤਾਵਰਣ ਨਾਮ ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ (ਡਿਫੌਲਟ: `langchain4j-dev`)

ਇਹ ਬਣਾਏਗਾ:
- GPT-5.2 ਅਤੇ text-embedding-3-small ਵਾਲਾ Azure OpenAI ਸਰੋਤ
- ਕਨੈਕਸ਼ਨ ਵੇਰਵੇ ਆਉਟਪੁੱਟ ਕਰੇਗਾ

### 2. ਕਨੈਕਸ਼ਨ ਵੇਰਵੇ ਪ੍ਰਾਪਤ ਕਰੋ

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

ਇਹ ਵੇਖਾਏਗਾ:
- `AZURE_OPENAI_ENDPOINT`: ਤੁਹਾਡਾ Azure OpenAI ਐਂਡਪੋਇੰਟ URL
- `AZURE_OPENAI_KEY`: ਪ੍ਰਮਾਣਿਕਤਾ ਲਈ API ਕੀ
- `AZURE_OPENAI_DEPLOYMENT`: ਚੈਟ ਮਾਡਲ ਦਾ ਨਾਮ (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: ਐਮਬੈੱਡਿੰਗ ਮਾਡਲ ਦਾ ਨਾਮ

### 3. ਐਪਲੀਕੇਸ਼ਨ ਸਥਾਨਕ ਤੌਰ 'ਤੇ ਚਲਾਓ

`azd up` ਕਮਾਂਡ ਆਪਣੇ ਆਪ ਮੂਲ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ ਸਾਰੇ ਜ਼ਰੂਰੀ ਵਾਤਾਵਰਣ ਪਰਿਵਰਤਨਸ਼ੀਲਾਂ ਵਾਲਾ `.env` ਫਾਇਲ ਬਣਾਉਂਦੀ ਹੈ।

**ਸਿਫਾਰਸ਼ ਕੀਤੀ:** ਸਾਰੇ ਵੈੱਬ ਐਪਲਿਕੇਸ਼ਨ ਸ਼ੁਰੂ ਕਰੋ:

**Bash:**
```bash
# ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਤੋਂ
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# ਰੂਟ ਡਿਰੈਕਟਰੀ ਤੋਂ
cd ../..
.\start-all.ps1
```

ਜਾਂ ਇੱਕ ਇਕੱਲਾ ਮੋਡੀਊਲ ਸ਼ੁਰੂ ਕਰੋ:

**Bash:**
```bash
# ਉਦਾਹਰਨ: ਸਿਰਫ਼ ਪਰਿਚਯ ਮੋਡੀਊਲ ਸ਼ੁਰੂ ਕਰੋ
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# ਉਦਾਹਰਨ: ਸਿਰਫ ਪਰਿਚਯ ਮੋਡੀਊਲ ਸ਼ੁਰੂ ਕਰੋ
cd ../01-introduction
.\start.ps1
```

ਦੋਹਾਂ ਸਕ੍ਰਿਪਟਾਂ `azd up` ਦੁਆਰਾ ਬਣਾਈ `.env` ਫਾਇਲ ਤੋਂ ਵਾਤਾਵਰਣ ਪਰਿਵਰਤਨਸ਼ੀਲ ਲੋਡ ਕਰਦੀਆਂ ਹਨ।

## ਸੰਰਚਨਾ

### ਮਾਡਲ ਤੈਅਕਰਨ ਨੂੰ ਕਸਟਮਾਈਜ਼ ਕਰਨਾ

ਮਾਡਲ ਤੈਅਕਰਨ ਬਦਲਣ ਲਈ, `infra/main.bicep` ਨੂੰ ਸੋਧੋ ਅਤੇ `openAiDeployments` ਪੈਰਾਮੀਟਰ ਸੋਧੋ:

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

ਉਪਲਬਧ ਮਾਡਲ ਅਤੇ ਵਰਜ਼ਨ: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure ਖੇਤਰ ਬਦਲਣਾ

ਕਿਸੇ ਹੋਰ ਖੇਤਰ ਵਿੱਚ ਤੈਅ ਕਰਨ ਲਈ, `infra/main.bicep` ਸੋਧੋ:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 ਉਪਲਬਧਤਾ ਦੇਖਣ ਲਈ: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep ਫਾਇਲਾਂ ਵਿੱਚ ਤਬਦੀਲੀਆਂ ਕਰਨ ਤੋਂ ਬਾਅਦ ਇੰਫਰਾਸਟ੍ਰੱਕਚਰ ਅਪਡੇਟ ਕਰਨ ਲਈ:

**Bash:**
```bash
# ARM ਟੈmplੇਟ ਨੂੰ ਮੁੜ ਬਣਾਓ
az bicep build --file infra/main.bicep

# ਬਦਲਾਵਾਂ ਦਾ ਪੂਰਵ ਦਰਸ਼ਨ ਕਰੋ
azd provision --preview

# ਬਦਲਾਵਾਂ ਲਾਗੂ ਕਰੋ
azd provision
```

**PowerShell:**
```powershell
# ARM ਟੈਮਪਲੇਟ ਨੂੰ ਮੁੜ ਬਣਾਓ
az bicep build --file infra/main.bicep

# ਬਦਲਾਅ ਦਾ ਪ੍ਰੀਵਿਊ ਕਰੋ
azd provision --preview

# ਬਦਲਾਅ ਲਾਗੂ ਕਰੋ
azd provision
```

## ਸਾਫ਼ ਸਫਾਈ

ਸਾਰੇ ਸਰੋਤ ਮਿਟਾਉਣ ਲਈ:

**Bash:**
```bash
# ਸਾਰੇ ਸਰੋਤ ਮਿਟਾਓ
azd down

# ਸਾਰਾ ਕੁਝ ਮਿਟਾਓ ਜਿਸ ਵਿੱਚ ਵਾਤਾਵਰਨ ਵੀ ਸ਼ਾਮਿਲ ਹੈ
azd down --purge
```

**PowerShell:**
```powershell
# ਸਾਰੇ ਸਰੋਤ ਹਟਾਓ
azd down

# ਮਾਹੌਲ ਸਮੇਤ ਸਾਰੀਆਂ ਚੀਜ਼ਾਂ ਮਿਟਾਓ
azd down --purge
```

**ਚੇਤਾਵਨੀ**: ਇਹ ਸਾਰੇ Azure ਸਰੋਤ ਸਥਾਈ ਤੌਰ 'ਤੇ ਮਿਟਾ ਦੇਵੇਗਾ।

## ਫਾਇਲ ਸੰਜੋਇਆ

## ਖਰਚ ਬਚਤ

### ਵਿਕਾਸ/ਟੈਸਟਿੰਗ
ਡਿਵੈਲਪਮੈਂਟ ਜਾਂ ਟੈਸਟਿੰਗ ਵਾਤਾਵਰਣਾਂ ਲਈ, ਤੁਸੀਂ ਖਰਚ ਘਟਾ ਸਕਦੇ ਹੋ:
- Azure OpenAI ਲਈ ਸਟੈਂਡਰਡ ਟੀਅਰ (S0) ਦੀ ਵਰਤੋਂ ਕਰੋ
- `infra/core/ai/cognitiveservices.bicep` ਵਿੱਚ ਸਮਰੱਥਾ ਘੱਟ ਕਰੋ (20K ਦੀ ਬਜਾਏ 10K TPM)
- ਵਰਤੋਂ ਨਾ ਹੋਣ ਸਮੇਂ ਸਰੋਤ ਮਿਟਾ ਦਿਓ: `azd down`

### ਪ੍ਰੋਡਕਸ਼ਨ
ਪ੍ਰੋਡਕਸ਼ਨ ਲਈ:
- ਵਰਤੋਂ ਦੇ ਮੁਤਾਬਕ OpenAI ਸਮਰੱਥਾ ਵਧਾਓ (50K+ TPM)
- ਵੱਡੀ ਉਪਲਬਧਤਾ ਲਈ ਜ਼ੋਨ ਰਿਡੰਡੰਸੀ ਚਾਲੂ ਕਰੋ
- ਢੰਗ ਨਾਲ ਮਾਨੀਟਰਿੰਗ ਅਤੇ ਖਰਚ ਅਲਰਟ ਲਾਗੂ ਕਰੋ

### ਖਰਚ ਅੰਦਾਜ਼ਾ
- Azure OpenAI: ਟੋਕਨ ਮੁਤਾਬਕ ਭੁਗਤਾਨ (ਇਨਪੁੱਟ + ਆਉਟਪੁੱਟ)
- GPT-5.2: ਲਗਭਗ $3-5 ਪ੍ਰਤੀ 1M ਟੋਕਨ (ਮੌਜੂਦਾ ਕੀਮਤ ਚੈੱਕ ਕਰੋ)
- text-embedding-3-small: ਲਗਭਗ $0.02 ਪ੍ਰਤੀ 1M ਟੋਕਨ

ਕੀਮਤ ਕੈਲਕੂਲੇਟਰ: https://azure.microsoft.com/pricing/calculator/

## ਮਾਨੀਟਰਿੰਗ

### Azure OpenAI ਮੈਟਰਿਕਸ ਦੇਖੋ

Azure ਪੋਰਟਲ 'ਤੇ ਜਾਓ → ਆਪਣਾ OpenAI ਸਰੋਤ → ਮੈਟਰਿਕਸ:
- ਟੋਕਨ ਅਧਾਰਿਤ ਵਰਤੋਂ
- HTTP ਬੇਨਤੀ ਦਰ
- ਜਵਾਬ ਦੇਣ ਲਈ ਸਮਾਂ
- ਸਰਗਰਮ ਟੋਕਨ

## ਸਮੱਸਿਆ ਨਿਵਾਰਣ

### ਸਮੱਸਿਆ: Azure OpenAI ਸਬਡੋਮੇਨ ਨਾਂ ਟਕਰਾਅ

**ਤਰੁੱਟੀ ਸੰਦੇਸ਼:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**ਕਾਰਨ:**
ਤੁਹਾਡੇ ਸਬਸਕ੍ਰਿਪਸ਼ਨ/ਵਾਤਾਵਰਣ ਤੋਂ ਬਣਿਆ ਸਬਡੋਮੇਨ ਨਾਂ ਪਹਿਲਾਂ ਹੀ ਵਰਤਿਆ ਜਾ ਰਿਹਾ ਹੈ, ਮੂੰਹਾਂ ਇੱਕ ਪਿਛਲੇ ਤੈਅਕਰਨ ਤੋਂ ਜੋ ਪੂਰੀ ਤਰ੍ਹਾਂ ਮਿਟਾਇਆ ਨਹੀਂ ਗਿਆ।

**ਹੁੱਲ:**
1. **ਚੋਣ 1 - ਵੱਖਰਾ ਵਾਤਾਵਰਣ ਨਾਮ ਵਰਤੋ:**
   
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

2. **ਚੋਣ 2 - Azure ਪੋਰਟਲ ਰਾਹੀਂ ਮੈਨੁਅਲ ਤੈਅਕਰਨ:**
   - Azure ਪੋਰਟਲ → ਇੱਕ ਸਰੋਤ ਬਣਾਓ → Azure OpenAI
   - ਤੁਹਾਡੇ ਸਰੋਤ ਲਈ ਇੱਕ ਵਿਲੱਖਣ ਨਾਮ ਚੁਣੋ
   - ਹੇਠ ਲਿਖੇ ਮਾਡਲਤੈਅ ਕਰੋ:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG ਮੋਡੀਊਲਾਂ ਲਈ)
   - **ਮਹੱਤਵਪੂਰਨ:** ਆਪਣੇ ਤੈਅਕਰਨ ਨਾਮ ਨੋਟ ਕਰੋ - ਇਹ `.env` ਸੰਰਚਨਾ ਨਾਲ ਮੇਲ ਖਾਣੇ ਚਾਹੀਦੇ ਹਨ
   - ਤੈਅ ਕਰਨ ਤੋਂ ਬਾਅਦ, "ਕੁੰਜੀਆਂ ਅਤੇ ਐਂਡਪੋਇਂਟ" ਤੋਂ ਆਪਣਾ ਐਂਡਪੋਇਂਟ ਅਤੇ API ਕੀ ਪ੍ਰਾਪਤ ਕਰੋ
   - ਪ੍ਰੋਜੈਕਟ ਰੂਟ ਵਿੱਚ `.env` ਫਾਇਲ ਬਣਾਓ ਜਿਸ ਵਿੱਚ:
     
     **ਉਦਾਹਰਨ `.env` ਫਾਇਲ:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**ਮਾਡਲ Deployment ਨਾਮਕਰਨ ਦਿਸ਼ਾ-ਨਿਰਦੇਸ਼:**
- ਸਧਾਰਣ ਤੇ ਅਖੰਡ ਨਾਮ ਵਰਤੋ: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Deployment ਨਾਮ ਪੂਰੀ ਤਰ੍ਹਾਂ `.env` ਵਿੱਚ ਦਰਜ ਕੀਤੇ ਨਾਮ ਨਾਲ ਮੇਲ ਖਾਣੇ ਚਾਹੀਦੇ ਹਨ
- ਆਮ ਗਲਤੀ: ਇੱਕ ਨਾਂ ਨਾਲ ਮਾਡਲ ਬਣਾਉਣਾ ਪਰ ਕੋਡ ਵਿੱਚ ਵੱਖਰਾ ਨਾਂ ਸੰਦર્ભਿਤ ਕਰਨਾ

### ਸਮੱਸਿਆ: ਚੁਣੇ ਹੋਏ ਖੇਤਰ ਵਿੱਚ GPT-5.2 ਉਪਲਬਧ ਨਹੀਂ

**ਹੁੱਲ:**
- GPT-5.2 ਐਕਸੈਸ ਵਾਲਾ ਖੇਤਰ ਚੁਣੋ ਜਿਵੇਂ `eastus2`
- ਉਪਲਬਧਤਾ ਚੈੱਕ ਕਰੋ: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### ਸਮੱਸਿਆ: ਤੈਅਕਰਨ ਲਈ ਪ੍ਰਚੁਰਤਾ (ਕੋਟਾ) ਘੱਟ ਹੈ

**ਹੁੱਲ:**
1. Azure ਪੋਰਟਲ ਵਿੱਚ ਪ੍ਰਚੁਰਤਾ ਵਾਧਾ ਬੇਨਤੀ ਕਰੋ
2. ਜਾਂ `main.bicep` ਵਿੱਚ ਘੱਟ ਸਮਰੱਥਾ ਵਰਤੋਂ (ਜਿਵੇਂ: ਸਮਰੱਥਾ: 10)

### ਸਮੱਸਿਆ: "Resource not found" ਜਦੋਂ ਸਥਾਨਕ ਚਲਾਇਆ ਜਾਵੇ

**ਹੁੱਲ:**
1. ਤੈਅਕਰਨ ਦੀ ਜਾਂਚ ਕਰੋ: `azd env get-values`
2. ਐਂਡਪੋਇੰਟ ਅਤੇ ਕੁੰਜੀ ਸਹੀ ਹਨ ਜਾਂ ਨਹੀਂ ਜਾਂਚੋ
3. ਯਕੀਨੀ ਕਰੋ ਕਿ ਸਰੋਤ ਗਰੁੱਪ Azure ਪੋਰਟਲ ਵਿੱਚ ਮੌਜੂਦ ਹੈ

### ਸਮੱਸਿਆ: ਪ੍ਰਮਾਣਿਕਤਾ ਅਸਫਲ

**ਹੁੱਲ:**
- ਯਕੀਨੀ ਬਣਾਓ ਕਿ `AZURE_OPENAI_API_KEY` ਸਹੀ ਤੌਰ 'ਤੇ ਸੈੱਟ ਹੈ
- ਕੁੰਜੀ ਫਾਰਮੈਟ 32-ਅੱਖਰੀ ਹੇਕਸਾਡੇਸੀਮਲ ਸਤਰ ਹੋਣਾ ਚਾਹੀਦਾ ਹੈ
- ਜ਼ਰੂਰਤ ਪੈਣ 'ਤੇ Azure ਪੋਰਟਲ ਤੋਂ ਨਵੀਂ ਕੁੰਜੀ ਪ੍ਰਾਪਤ ਕਰੋ

### ਤੈਅਕਰਨ ਅਸਫਲ

**ਸਮੱਸਿਆ**: `azd provision` quota ਜਾਂ ਸਮਰੱਥਾ ਗਲਤੀਆਂ ਨਾਲ ਅਸਫਲ

**ਹੁੱਲ**: 
1. ਵੱਖਰੇ ਖੇਤਰ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ - ਖੇਤਰ ਸੰਬੰਧੀ ਸੰਰਚਨਾ ਲਈ [Changing Azure Regions](../../../../01-introduction/infra) ਸੈਕਸ਼ਨ ਵੇਖੋ
2. ਯਕੀਨੀ ਕਰੋ ਤੁਹਾਡੇ ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਕੋਲ Azure OpenAI quota ਹੈ:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### ਐਪਲੀਕੇਸ਼ਨ ਕੋਨੈਕਟ ਨਹੀਂ ਕਰ ਰਿਹਾ

**ਸਮੱਸਿਆ**: ਜਾਵਾ ਐਪਲੀਕੇਸ਼ਨ ਕੰਨੈਕਸ਼ਨ ਗਲਤੀਆਂ ਦਿਖਾ ਰਿਹਾ ਹੈ

**ਹੁੱਲ**:
1. ਵਾਤਾਵਰਣ ਪਰਿਵਰਤਨਸ਼ੀਲਾਂ ਐਕਸਪੋਰਟ ਹੋਏ ਹੋਣ ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ:
   
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

2. ਐਂਡਪੋਇੰਟ ਫਾਰਮੈਟ ਸਹੀ ਹੈ ਜਾਂ ਨਹੀਂ (ਉਦਾਹਰਨ: `https://xxx.openai.azure.com`)
3. ਯਕੀਨੀ ਬਣਾਓ ਕਿ API ਕੁੰਜੀ Azure ਪੋਰਟਲ ਤੋਂ ਪ੍ਰਾਇਮਰੀ ਜਾਂ ਸੈਕੰਡਰੀ ਕੁੰਜੀ ਹੈ

**ਸਮੱਸਿਆ**: Azure OpenAI ਤੋਂ 401 Unauthorized

**ਹੁੱਲ**:
1. Azure ਪੋਰਟਲ → Keys and Endpoint ਤੋਂ ਨਵੀਂ API ਕੁੰਜੀ ਲਵੋ
2. `AZURE_OPENAI_API_KEY` ਵਾਤਾਵਰਣ ਪਰਿਵਰਤਨਸ਼ੀਲ ਨਵਾਂ ਐਕਸਪੋਰਟ ਕਰੋ
3. ਮਾਡਲ ਤੈਅਕਰਨ ਪੂਰੇ ਹੋਣ ਦੀ ਜਾਂਚ (Azure ਪੋਰਟਲ ਵੇਖੋ)

### ਪ੍ਰਦਰਸ਼ਨ ਸਮੱਸਿਆਵਾਂ

**ਸਮੱਸਿਆ**: ਜਵਾਬ ਦੇਣ ਵਿੱਚ ਧੀਰਜ

**ਹੁੱਲ**:
1. Azure ਪੋਰਟਲ ਮੈਟਰਿਕਸ ਵਿੱਚ OpenAI ਟੋਕਨ ਵਰਤੋਂ ਅਤੇ ਥਰੌਟਲਿੰਗ ਦੀ ਜਾਂਚ ਕਰੋ
2. ਜੇ ਤੁਸੀਂ ਸੀਮਾਵਾਂ 'ਤੇ ਹੋ ਤਾਂ TPM ਸਮਰੱਥਾ ਵਧਾਓ
3. ਵੱਧ ਸੋਚ-ਵਿਚਾਰ ਦੇ ਪੱਧਰ (ਕਮ/ਦਰਮਿਆਨਾ/ਵੱਧ) ਵਰਤਣ 'ਤੇ ਵਿਚਾਰ ਕਰੋ

## ਇੰਫਰਾਸਟ੍ਰੱਕਚਰ ਅਪਡੇਟ ਕਰਨਾ

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

## ਸੁਰੱਖਿਆ ਦੀਆਂ ਸਿਫਾਰਸ਼ਾਂ

1. **ਕਦੇ ਵੀ API ਕੁੰਜੀਆਂ ਕਮਿੱਟ ਨਾ ਕਰੋ** - ਵਾਤਾਵਰਣ ਪਰਿਵਰਤਨਸ਼ੀਲ ਵਰਤੋਂ ਕਰੋ
2. **ਲੋਕਲ ਤੌਰ 'ਤੇ .env ਫਾਇਲ ਵਰਤੋਂ** - `.env` ਨੂੰ `.gitignore` ਵਿੱਚ ਸ਼ਾਮਲ ਕਰੋ
3. **ਕੁੰਜੀਆਂ ਨਿਯਮਤ ਤੌਰ 'ਤੇ ਰੋਟੇਟ ਕਰੋ** - Azure ਪੋਰਟਲ ਵਿੱਚ ਨਵੀਆਂ ਕੁੰਜੀਆਂ ਬਣਾਓ
4. **ਪਹੁੰਚ ਸੀਮਤ ਕਰੋ** - ਸਰੋਤਾਂ ਦੀ ਪਹੁੰਚ Azure RBAC ਨਾਲ ਨiyanਤਰਿਤ ਕਰੋ
5. **ਵਰਤੋਂ ਮਾਨੀਟਰ ਕਰੋ** - Azure ਪੋਰਟਲ ਵਿੱਚ ਖਰਚ ਅਲਰਟ ਲਗਾਓ

## ਵਾਧੂ ਸਾਧਨ

- [Azure OpenAI ਸੇਵਾ ਦਸਤਾਵੇਜ਼](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 ਮਾਡਲ ਦਸਤਾਵੇਜ਼](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI ਦਸਤਾਵੇਜ਼](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep ਦਸਤਾਵੇਜ਼](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI ਅਧਿਕਾਰਿਕ ਇੰਟਿਗ੍ਰੇਸ਼ਨ](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## ਸਹਾਇਤਾ

ਸਮੱਸਿਆ ਲਈ:
1. ਉਪਰ [ਸਮੱਸਿਆ ਨਿਵਾਰਣ ਸੈਕਸ਼ਨ](../../../../01-introduction/infra) ਵਿਚ ਦੇਖੋ
2. Azure ਪੋਰਟਲ ਵਿੱਚ Azure OpenAI ਸੇਵਾ ਸਿਹਤ ਸੰਭਾਲੋ
3. ਰਿਪੋਜਿਟਰੀ ਵਿੱਚ ਇੱਕ ਮਾਮਲਾ ਖੋਲ੍ਹੋ

## ਲਾਇਸੈਂਸ

ਵਿਸਥਾਰ ਲਈ ਮੂਲ [LICENSE](../../../../LICENSE) ਫਾਇਲ ਵੇਖੋ।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕਾਰਿਆ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਦਿਓ ਕਿ ਸੁਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸੁਚਿਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੁਲ਼ ਦਸਤਾਵੇਜ਼ ਨੂੰ ਇਸ ਦੀ ਮਾਤ ਭਾਸ਼ਾ ਵਿੱਚ ਪ੍ਰਮਾਣਿਕ ਸਰੋਤ ਵਜੋਂ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਅਹੰਕਾਰਕ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੂਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਨਾਲ ਉੱਪਜਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤ ਫਹਿਮੀਆਂ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆਵਾਂ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹੋਵਾਂਗੇ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->